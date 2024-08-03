document.addEventListener('DOMContentLoaded', function() {
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

    // Add event listener to the login form
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', function(event) {
            event.preventDefault();
            const username = document.getElementById('username').value;
            const password = document.getElementById('password').value;

            fetch('/api/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ username, password })
            })
                .then(response => response.json())
                .then(data => {
                    alert(data.message);
                    if (data.message === 'Login successful') {
                        localStorage.setItem('isLoggedIn', 'true');
                        localStorage.setItem('username', username);
                        window.location.href = 'home.html';
                    }
                })
                .catch(error => console.error('Error:', error));
        });
    }

    // Add event listener to the register form
    const registerForm = document.getElementById('registerForm');
    if (registerForm) {
        registerForm.addEventListener('submit', function(event) {
            event.preventDefault();
            const formData = new FormData(registerForm);
            const jsonData = {};
            formData.forEach((value, key) => { jsonData[key] = value });

            fetch('/api/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(jsonData)
            })
                .then(response => response.json())
                .then(data => {
                    alert(data.message);
                    if (data.message === 'User registered successfully') {
                        window.location.href = 'login.html';
                    }
                })
                .catch(error => console.error('Error:', error));
        });
    }

    // Load quiz questions
    const questionsDiv = document.getElementById('questions');
    if (questionsDiv) {
        fetch('/api/quizzes')
            .then(response => response.json())
            .then(data => {
                data.forEach((quiz, index) => {
                    const questionDiv = document.createElement('div');
                    questionDiv.textContent = (index + 1) + '. ' + quiz.question;
                    questionsDiv.appendChild(questionDiv);
                });
            })
            .catch(error => console.error('Error:', error));
    }

    // Add event listener to the quiz submit button
    const submitQuizButton = document.getElementById('submitQuiz');
    if (submitQuizButton) {
        submitQuizButton.addEventListener('click', function() {
            alert('Quiz submitted');
        });
    }
});
