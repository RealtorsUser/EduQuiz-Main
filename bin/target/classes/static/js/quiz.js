document.addEventListener('DOMContentLoaded', function() {
    let currentQuestionIndex = 0;
    let timer;
    let timeLeft = 20 * 60; // 20 minutes in seconds
    const questions = [];

    const startQuizButton = document.getElementById('startQuiz');
    const timerElement = document.getElementById('timer');
    const questionsDiv = document.getElementById('questions');
    const questionNumbersDiv = document.getElementById('questionNumbers');
    const prevButton = document.getElementById('prevQuestion');
    const nextButton = document.getElementById('nextQuestion');
    const finishButton = document.getElementById('finishTest');

    function startTimer() {
        timer = setInterval(() => {
            if (timeLeft <= 0) {
                clearInterval(timer);
                alert('Time is up!');
                // Submit quiz automatically
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

    function loadQuestions() {
        fetch('/api/quizzes')
            .then(response => response.json())
            .then(data => {
                questions.push(...data);
                displayQuestion(currentQuestionIndex);
                setupQuestionNumbers();
            })
            .catch(error => console.error('Error:', error));
    }

    function displayQuestion(index) {
        questionsDiv.innerHTML = '';
        const question = questions[index];
        const questionElement = document.createElement('div');
        questionElement.textContent = `${index + 1}. ${question.question}`;
        question.options.forEach((option, i) => {
            const optionElement = document.createElement('div');
            optionElement.textContent = `${String.fromCharCode(65 + i)}) ${option}`;
            questionElement.appendChild(optionElement);
        });
        questionsDiv.appendChild(questionElement);
        updateQuestionNumberHighlight();
        updateNavigationButtons();
    }

    function setupQuestionNumbers() {
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
            if (index === currentQuestionIndex) {
                num.classList.add('active');
            } else {
                num.classList.remove('active');
            }
        });
    }

    function updateNavigationButtons() {
        prevButton.disabled = currentQuestionIndex === 0;
        nextButton.disabled = currentQuestionIndex === questions.length - 1;
    }

    function submitQuiz() {
        // Add your quiz submission logic here
        alert('Quiz submitted');
    }

    startQuizButton.addEventListener('click', function() {
        startQuizButton.disabled = true;
        startTimer();
        loadQuestions();
    });

    prevButton.addEventListener('click', function() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            displayQuestion(currentQuestionIndex);
        }
    });

    nextButton.addEventListener('click', function() {
        if (currentQuestionIndex < questions.length - 1) {
            currentQuestionIndex++;
            displayQuestion(currentQuestionIndex);
        }
    });

    finishButton.addEventListener('click', function() {
        clearInterval(timer);
        submitQuiz();
    });

    // Check login status
    function checkLoginStatus() {
        const isLoggedIn = localStorage.getItem('isLoggedIn') === 'true';
        const username = localStorage.getItem('username');
        const authSection = document.getElementById('auth-section');
        const profileSection = document.getElementById('profile-section');
        const usernameSpan = document.getElementById('username');

        if (isLoggedIn) {
            authSection.style.display = 'none';
            profileSection.style.display = 'block';
            usernameSpan.textContent = username;
        } else {
            authSection.style.display = 'block';
            profileSection.style.display = 'none';
        }
    }

    // Check login status on page load
    checkLoginStatus();

    // Logout functionality
    document.getElementById('logout').addEventListener('click', function() {
        localStorage.removeItem('isLoggedIn');
        localStorage.removeItem('username');
        window.location.href = 'login.html';
    });
});
