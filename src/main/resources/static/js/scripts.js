document.addEventListener('DOMContentLoaded', function() {
    let currentQuestionIndex = 0;
    let timer;
    let timeLeft = 20 * 60; // 20 minutes in seconds
    let questions = [];
    const answers = []; // Array to store user answers
    const username = document.getElementById('QuizUser').textContent.trim();

    const startQuizButton = document.getElementById('startQuiz');
    const timerElement = document.getElementById('timer');
    const questionsDiv = document.getElementById('questions');
    const questionNumbersDiv = document.getElementById('questionNumbers');
    const prevButton = document.getElementById('prevQuestion');
    const nextButton = document.getElementById('nextQuestion');
    const finishButton = document.getElementById('finishTest');

    async function fetchQuestions() {
        try {
            const response = await fetch('https://docs.google.com/spreadsheets/d/e/2PACX-1vR97v2xZ5tYvKJvzIuMklYQcr1uVFXYJcjGdnU1SayjzvmtcjAt6V8r1xByetvGopeKCkRjV5R4bZIf/pub?output=csv');
            const csvText = await response.text();
            questions = parseCSV(csvText).slice(0, 25); // Take 25 random questions
            displayQuestion(currentQuestionIndex);
            setupQuestionNumbers();
        } catch (error) {
            console.error('Error fetching questions:', error);
        }
    }

    function parseCSV(csvText) {
        const rows = csvText.split('\n').slice(1); // Ignore the header row
        return rows.map(row => {
            const columns = row.split(',');
            return {
                questionText: columns[0],
                options: [columns[1], columns[2], columns[3], columns[4]],
                correctOption: columns[5].trim()
            };
        });
    }

    function startTimer() {
        timer = setInterval(() => {
            if (timeLeft <= 0) {
                clearInterval(timer);
                alert('Time is up!');
                submitQuiz();
            } else {
                timeLeft--;
                updateTimerDisplay();
            }
        }, 1000);
    }

    function updateTimerDisplay() {
        const minutes = Math.floor(timeLeft / 60);
        const seconds = timeLeft % 60;
        timerElement.textContent = `${minutes}:${seconds < 10 ? '0' : ''}${seconds}`;
    }

    function displayQuestion(index) {
        if (index < 0 || index >= questions.length) return;

        const question = questions[index];
        questionsDiv.innerHTML = `
            <h2>${index + 1}. ${question.questionText}</h2>
            ${question.options.map((option, i) => `
                <div>
                    <input type="radio" id="option${i}" name="answer" value="${option}">
                    <label for="option${i}">${option}</label>
                </div>
            `).join('')}
        `;

        updateQuestionNumberHighlight();
        updateNavigationButtons();
    }

    function setupQuestionNumbers() {
        questionNumbersDiv.innerHTML = ''; // Clear previous numbers
        questions.forEach((_, index) => {
            const numberElement = document.createElement('div');
            numberElement.classList.add('question-number');
            numberElement.textContent = index + 1;
            numberElement.addEventListener('click', () => {
                currentQuestionIndex = index;
                displayQuestion(currentQuestionIndex);
            });
            questionNumbersDiv.appendChild(numberElement);
        });
    }

    function updateQuestionNumberHighlight() {
        const numbers = document.querySelectorAll('.question-number');
        numbers.forEach((num, index) => {
            num.classList.toggle('active', index === currentQuestionIndex);
        });
    }

    function updateNavigationButtons() {
        prevButton.disabled = currentQuestionIndex === 0;
        nextButton.disabled = currentQuestionIndex === questions.length - 1;
    }

    function collectAnswers() {
        const selectedOption = document.querySelector('input[name="answer"]:checked');
        if (selectedOption) {
            answers[currentQuestionIndex] = selectedOption.value;
        }
    }

    async function submitQuiz() {
        collectAnswers();

        // Validate answers and calculate score
        const score = answers.reduce((acc, answer, index) => {
            return acc + (answer === questions[index].correctOption ? 1 : 0);
        }, 0);

        // Save result to the Google Sheets
        await saveResult(score);
        // Hide quiz-related elements
        document.getElementById('timer').style.display = 'none';
        document.getElementById('startQuiz').style.display = 'none';
        document.getElementById('questionNumbers').style.display = 'none';
        document.getElementById('questions').style.display = 'none';
        document.getElementById('prevQuestion').style.display = 'none';
        document.getElementById('nextQuestion').style.display = 'none';
        document.getElementById('finishTest').style.display = 'none';

        // Show result container
        const resultContainer = document.getElementById('result');
        resultContainer.style.display = 'block';
        resultContainer.innerHTML = `<h2>Your Score: ${score}</h2>`;
    }

    async function saveResult(score) {
        const username = document.getElementById('QuizUser').textContent.trim();

        const scriptURL = 'https://script.google.com/macros/s/AKfycbyhzFvO1gzeZyBEx8vMJ6p8fjSY2czal-uJ5rhkUyMQoUBbdbx-hvt1nPQx1z4s5s1Jjw/exec'; // Replace with the actual URL

        const resultData = {
            username: username,
            score: score
        };

        try {
            const response = await fetch(scriptURL, {
                method: 'POST',
                body: JSON.stringify(resultData),
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            const result = await response.json();

            if (result.status === 'success') {
                console.log("Result saved successfully.");
            } else {
                console.error("Error saving result:", result.message);
            }
        } catch (error) {
            console.error('Error:', error);
        }
    }


    startQuizButton.addEventListener('click', function() {
        startQuizButton.disabled = true;
        fetchQuestions().then(() => {
            startTimer();
            displayQuestion(currentQuestionIndex);
        });
    });

    prevButton.addEventListener('click', function() {
        collectAnswers();
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            displayQuestion(currentQuestionIndex);
        }
    });

    nextButton.addEventListener('click', function() {
        collectAnswers();
        if (currentQuestionIndex < questions.length - 1) {
            currentQuestionIndex++;
            displayQuestion(currentQuestionIndex);
        }
    });

    finishButton.addEventListener('click', async function() {
        clearInterval(timer); // Stop the timer
        collectAnswers(); // Ensure the last question's answer is recorded
        await submitQuiz(); // Validate, display, and save the result
    });

    // Initialize the quiz with login status check
    if (username === '') {
        alert('Please log in to take the quiz.');
        window.location.href = '/login';
    }
});


