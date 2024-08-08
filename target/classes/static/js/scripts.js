document.addEventListener('DOMContentLoaded', function() {
    let currentQuestionIndex = 0;
    let timer;
    let timeLeft = 20 * 60; // 20 minutes in seconds
    let questions = [];
    const answers = []; // Array to store user answers

    const startQuizButton = document.getElementById('startQuiz');
    const timerElement = document.getElementById('timer');
    const questionsDiv = document.getElementById('questions');
    const questionNumbersDiv = document.getElementById('questionNumbers');
    const prevButton = document.getElementById('prevQuestion');
    const nextButton = document.getElementById('nextQuestion');
    const finishButton = document.getElementById('finishTest');

    async function fetchQuestions() {
        try {
            const response = await fetch('/api/quizzes');
            if (response.ok) {
                questions = await response.json();
                displayQuestion(currentQuestionIndex);
                setupQuestionNumbers();
            } else {
                console.error('Failed to fetch questions');
            }
        } catch (error) {
            console.error('Error fetching questions:', error);
        }
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
            <h2>${index + 1}. ${question.question}</h2>
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
        const submission = answers.map((answer, index) => ({
            questionId: questions[index].id,
            selectedAnswer: answer
        }));

        try {
            const response = await fetch('/api/quizzes/submit', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ answers: submission })
            });

            const result = await response.json();
            alert(`Your score: ${result.score}/${result.totalQuestions}`);
        } catch (error) {
            console.error('Error submitting quiz:', error);
        }
    }

    function checkLoginStatus() {
        const isLoggedIn = localStorage.getItem('isLoggedIn') === 'true';
        const username = localStorage.getItem('username');
        const authSection = document.getElementById('auth-section');
        const profileSection = document.getElementById('profile-section');
        const usernameSpan = document.getElementById('username');

        if (!isLoggedIn) {
            window.location.href = 'login.html';
        } else {
            authSection.style.display = 'none';
            profileSection.style.display = 'block';
            usernameSpan.textContent = username;
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

    finishButton.addEventListener('click', function() {
        clearInterval(timer);
        collectAnswers();
        submitQuiz();
    });

    checkLoginStatus();

    document.getElementById('logout').addEventListener('click', function() {
        localStorage.removeItem('isLoggedIn');
        localStorage.removeItem('username');
        window.location.href = 'login.html';
    });
});
