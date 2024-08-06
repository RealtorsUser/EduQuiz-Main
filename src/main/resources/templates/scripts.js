document.addEventListener('DOMContentLoaded', function() {
    let currentQuestionIndex = 0;
    let timer;
    let timeLeft = 20 * 60; // 20 minutes in seconds
    const questions = [
        {
            "question": "What is the process by which plants make their own food using sunlight called?",
            "options": ["Respiration", "Photosynthesis", "Fermentation", "Digestion"],
            "answer": "Photosynthesis"
        },
        {
            "question": "What is the chemical formula for water?",
            "options": ["CO2", "H2O", "NaCl", "O2"],
            "answer": "H2O"
        },
        {
            "question": "Which gas is most abundant in the Earth's atmosphere?",
            "options": ["Oxygen", "Nitrogen", "Carbon Dioxide", "Hydrogen"],
            "answer": "Nitrogen"
        },
        {
            "question": "What organ is responsible for pumping blood throughout the body?",
            "option": ["Heart (Correct)", "Brain", "Liver", "Kidney"],
            "answer": "Heart"
        },
        {

            "Question": "What force keeps us on the ground and prevents us from floating into space?",
            "option": ["Magnetism", "Gravity (Correct)", "Friction", "Buoyancy"],
            "answe": "Gravity"
        }

// Add more questions here
    ];

    const startQuizButton = document.getElementById('startQuiz');
    const timerElement = document.getElementById('timer');
    const questionsDiv = document.getElementById('questions');
    const questionNumbersDiv = document.getElementById('questionNumbers');
    const prevButton = document.getElementById('prevQuestion');
    const nextButton = document.getElementById('nextQuestion');
    const finishButton = document.getElementById('finishTest');

    // Fetch results from the backend
        fetchResults();
        fetchTopPerformers();


    function fetchResults() {
        fetch('/results')
            .then(response => response.json())
            .then(data => {
                const resultsTbody = document.getElementById('results-tbody');
                resultsTbody.innerHTML = '';
                data.forEach(result => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                    <td>${result.id}</td>
                    <td>${result.quizId}</td>
                    <td>${result.score}</td>
                    <td>${result.user.username}</td>
                    <td>${result.user.school.name}</td>
                    <td>${result.coupon}</td>
                `;
                    resultsTbody.appendChild(row);
                });
            })
            .catch(error => console.error('Error fetching results:', error));
    }

    function fetchTopPerformers() {
        fetch('/results/top-performers')
            .then(response => response.json())
            .then(data => {
                const topPerformersTbody = document.getElementById('top-performers-tbody');
                topPerformersTbody.innerHTML = '';
                data.forEach(result => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                    <td>${result.id}</td>
                    <td>${result.quizId}</td>
                    <td>${result.score}</td>
                    <td>${result.user.username}</td>
                    <td>${result.user.school.name}</td>
                    <td>${result.prize}</td>
                    <td>${result.coupon}</td>
                `;
                    topPerformersTbody.appendChild(row);
                });
            })
            .catch(error => console.error('Error fetching top performers:', error));
    }


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

    function checkLoginStatus() {
        const isLoggedIn = localStorage.getItem('isLoggedIn') === 'true';
        const username = localStorage.getItem('username');
        const authSection = document.getElementById('auth-section');
        const profileSection = document.getElementById('profile-section');
        const usernameSpan = document.getElementById('username');

        if (!isLoggedIn) {
            // Redirect to login page if not logged in
            window.location.href = 'login.html';
            return;
        }

        if (isLoggedIn) {
            authSection.style.display = 'none';
            profileSection.style.display = 'block';
            usernameSpan.textContent = username;
        } else {
            authSection.style.display = 'block';
            profileSection.style.display = 'none';
        }
    }

    startQuizButton.addEventListener('click', function() {
        startQuizButton.disabled = true;
        startTimer();
        displayQuestion(currentQuestionIndex);
        setupQuestionNumbers();
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

    // Check login status on page load
     checkLoginStatus();

    // Fetch results and top performers when the page loads
    fetchResults();
    fetchTopPerformers();

    // Logout functionality
    document.getElementById('logout').addEventListener('click', function() {
        localStorage.removeItem('isLoggedIn');
        localStorage.removeItem('username');
        window.location.href = 'login.html';
    });
});
