document.addEventListener('DOMContentLoaded', () => {

    // --- Navigation Logic ---
    const navBtns = document.querySelectorAll('.nav-btn');
    const sections = document.querySelectorAll('.page-section');

    function navigateTo(targetId) {
        // Update Buttons
        navBtns.forEach(btn => {
            if (btn.dataset.target === targetId) {
                btn.classList.add('active');
            } else {
                btn.classList.remove('active');
            }
        });

        // Show Section
        sections.forEach(sec => {
            if (sec.id === targetId) {
                sec.classList.remove('hidden');
                sec.classList.add('active');
            } else {
                sec.classList.add('hidden');
                sec.classList.remove('active');
            }
        });
    }

    navBtns.forEach(btn => {
        btn.addEventListener('click', () => {
            navigateTo(btn.dataset.target);
        });
    });

    // Expose for onclick in HTML
    window.navTo = navigateTo;


    // --- Pomodoro Timer ---
    let timeLeft = 25 * 60;
    let timerId = null;
    let isRunning = false;

    const minEl = document.getElementById('minutes');
    const secEl = document.getElementById('seconds');
    const startBtn = document.getElementById('start-btn');
    const pauseBtn = document.getElementById('pause-btn');
    const resetBtn = document.getElementById('reset-btn');
    const modeBtns = document.querySelectorAll('.mode-btn');

    function updateDisplay() {
        const m = Math.floor(timeLeft / 60);
        const s = timeLeft % 60;
        minEl.textContent = m.toString().padStart(2, '0');
        secEl.textContent = s.toString().padStart(2, '0');
    }

    function startTimer() {
        if (isRunning) return;
        isRunning = true;
        startBtn.classList.add('hidden'); // or disable
        pauseBtn.style.display = 'inline-block'; // show pause
        startBtn.style.display = 'none';

        timerId = setInterval(() => {
            if (timeLeft > 0) {
                timeLeft--;
                updateDisplay();
            } else {
                clearInterval(timerId);
                isRunning = false;
                alert("Time's up! Take a break.");
                resetUI();
            }
        }, 1000);
    }

    function pauseTimer() {
        if (!isRunning) return;
        clearInterval(timerId);
        isRunning = false;
        resetUI();
    }

    function resetTimer() {
        pauseTimer();
        // find active mode
        const activeMode = document.querySelector('.mode-btn.active');
        timeLeft = parseInt(activeMode.dataset.time) * 60;
        updateDisplay();
    }

    function resetUI() {
        startBtn.style.display = 'inline-block';
        pauseBtn.style.display = 'none';
        isRunning = false;
    }

    // Init UI state
    pauseBtn.style.display = 'none';

    startBtn.addEventListener('click', startTimer);
    pauseBtn.addEventListener('click', pauseTimer);
    resetBtn.addEventListener('click', resetTimer);

    // Mode Switching
    modeBtns.forEach(btn => {
        btn.addEventListener('click', () => {
            modeBtns.forEach(b => b.classList.remove('active'));
            btn.classList.add('active');

            pauseTimer();
            timeLeft = parseInt(btn.dataset.time) * 60;
            updateDisplay();
        });
    });


    // --- Chat Logic ---
    const chatBox = document.getElementById('chat-box');
    const msgInput = document.getElementById('message-input');
    const sendBtn = document.getElementById('send-btn');

    function sendMessage() {
        const text = msgInput.value.trim();
        if (!text) return;

        // Create Message HTML
        const msgDiv = document.createElement('div');
        msgDiv.className = 'message sent';

        const now = new Date();
        const timeStr = now.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });

        msgDiv.innerHTML = `
            <p>${text}</p>
            <span class="time">${timeStr}</span>
        `;

        chatBox.appendChild(msgDiv);
        msgInput.value = '';

        // Scroll to bottom
        chatBox.scrollTop = chatBox.scrollHeight;

        // Simulate Reply
        setTimeout(() => {
            const replyDiv = document.createElement('div');
            replyDiv.className = 'message received';

            const responseText = getBotResponse(text);

            replyDiv.innerHTML = `
                <p>${responseText}</p>
                <span class="time">${new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}</span>
            `;
            chatBox.appendChild(replyDiv);
            chatBox.scrollTop = chatBox.scrollHeight;
        }, 1000 + Math.random() * 2000); // Random delay 1-3s
    }

    function getBotResponse(input) {
        const lowerInput = input.toLowerCase();

        if (lowerInput.includes('hello') || lowerInput.includes('hi') || lowerInput.includes('hey')) {
            return "Hey there! Ready to focus?";
        }
        if (lowerInput.includes('help') || lowerInput.includes('stuck')) {
            return "Don't worry! Take a deep breath. Break the problem down into smaller steps.";
        }
        if (lowerInput.includes('tired') || lowerInput.includes('break') || lowerInput.includes('exhausted')) {
            return "Sounds like you need a Pomodoro break! 5 minutes can do wonders.";
        }
        if (lowerInput.includes('goal') || lowerInput.includes('plan')) {
            return "That's the spirit! Write it down in your session goals.";
        }

        // Default Responses
        const responses = [
            "Nice! Keep going.",
            "You're doing great!",
            "Stay focused, you got this!",
            "One step at a time.",
            "Make sure to drink some water!",
            "Consistency is key! 🔑"
        ];
        return responses[Math.floor(Math.random() * responses.length)];
    }


    sendBtn.addEventListener('click', sendMessage);
    msgInput.addEventListener('keypress', (e) => {
        if (e.key === 'Enter') sendMessage();
    });

    // --- Task List Logic ---
    const taskInput = document.getElementById('new-task-input');
    const addTaskBtn = document.getElementById('add-task-btn');
    const taskList = document.getElementById('task-list');

    function addTask() {
        const text = taskInput.value.trim();
        if (!text) return;

        const li = document.createElement('li');
        const id = 'task-' + Date.now();
        li.innerHTML = `
            <input type="checkbox" id="${id}">
            <label for="${id}">${text}</label>
            <button class="delete-btn" style="background:none; border:none; color:#ef4444; margin-left:auto; cursor:pointer;"><i class="fa-solid fa-trash"></i></button>
        `;

        // Add delete functionality
        li.querySelector('.delete-btn').addEventListener('click', () => {
            li.remove();
        });

        taskList.appendChild(li);
        taskInput.value = '';
    }

    addTaskBtn.addEventListener('click', addTask);
    taskInput.addEventListener('keypress', (e) => {
        if (e.key === 'Enter') addTask();
    });

    // --- Music Player Logic ---
    const musicToggle = document.getElementById('music-toggle');
    let isPlaying = false;

    // Using a royalty-free track from Free Music Archive (Chad Crouch - Algorithms)
    // Source: https://freemusicarchive.org/music/ccCommunity/Chad_Crouch/Arps/Chad_Crouch_-_Algorithms/
    const audio = new Audio('https://files.freemusicarchive.org/storage-freemusicarchive-org/music/ccCommunity/Chad_Crouch/Arps/Chad_Crouch_-_Algorithms.mp3');
    audio.loop = true;
    audio.volume = 0.5;

    musicToggle.addEventListener('click', () => {
        isPlaying = !isPlaying;
        const icon = musicToggle.querySelector('i');
        if (isPlaying) {
            icon.classList.remove('fa-play');
            icon.classList.add('fa-pause');
            audio.play().catch(e => {
                console.error("Audio playback failed:", e);
                // Reset UI if playback fails (e.g., policy restriction)
                isPlaying = false;
                icon.classList.remove('fa-pause');
                icon.classList.add('fa-play');
                alert("Playback failed. Please interact with the document first or check connection.");
            });
        } else {
            icon.classList.remove('fa-pause');
            icon.classList.add('fa-play');
            audio.pause();
        }
    });

    // --- 3D Tilt Effect ---
    const cards = document.querySelectorAll('.card, .glass-panel');

    cards.forEach(card => {
        card.addEventListener('mousemove', (e) => {
            const rect = card.getBoundingClientRect();
            const x = e.clientX - rect.left;
            const y = e.clientY - rect.top;

            const centerX = rect.width / 2;
            const centerY = rect.height / 2;

            const rotateX = ((y - centerY) / centerY) * -5; // Max rotation deg
            const rotateY = ((x - centerX) / centerX) * 5;

            card.style.transform = `perspective(1000px) rotateX(${rotateX}deg) rotateY(${rotateY}deg)`;
        });

        card.addEventListener('mouseleave', () => {
            card.style.transform = 'perspective(1000px) rotateX(0) rotateY(0)';
        });
    });


    // --- Video Call Logic ---
    const videoBtn = document.getElementById('video-btn');
    const videoModal = document.getElementById('video-modal');
    const endCallBtn = document.getElementById('end-call-btn');
    const muteBtn = document.getElementById('mute-btn');
    const camBtn = document.getElementById('cam-btn');

    if (videoBtn && videoModal) {
        videoBtn.addEventListener('click', () => {
            videoModal.classList.remove('hidden');
        });

        endCallBtn.addEventListener('click', () => {
            videoModal.classList.add('hidden');
        });

        // Toggle Mute
        let isMuted = false;
        muteBtn.addEventListener('click', () => {
            isMuted = !isMuted;
            const icon = muteBtn.querySelector('i');
            if (isMuted) {
                muteBtn.classList.add('muted');
                icon.classList.remove('fa-microphone');
                icon.classList.add('fa-microphone-slash');
            } else {
                muteBtn.classList.remove('muted');
                icon.classList.remove('fa-microphone-slash');
                icon.classList.add('fa-microphone');
            }
        });

        // Toggle Camera
        let isCamOff = false;
        camBtn.addEventListener('click', () => {
            isCamOff = !isCamOff;
            const icon = camBtn.querySelector('i');
            if (isCamOff) {
                camBtn.classList.add('muted'); // Reuse muted style for off state
                icon.classList.remove('fa-video');
                icon.classList.add('fa-video-slash');
            } else {
                camBtn.classList.remove('muted');
                icon.classList.remove('fa-video-slash');
                icon.classList.add('fa-video');
            }
        });
    }


    // --- Login Logic ---
    const loginScreen = document.getElementById('login-screen');
    const appContent = document.getElementById('app-content');
    const emailInput = document.getElementById('email-input');
    const getOtpBtn = document.getElementById('get-otp-btn');
    const step1 = document.getElementById('login-step-1');
    const step2 = document.getElementById('login-step-2');
    const otpInput = document.getElementById('otp-input');
    const verifyOtpBtn = document.getElementById('verify-otp-btn');
    const backToEmailBtn = document.getElementById('back-to-email');

    // Simple Email Validation
    function isValidEmail(email) {
        return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
    }

    let generatedOtp = null;

    if (getOtpBtn) {
        getOtpBtn.addEventListener('click', () => {
            const email = emailInput.value.trim();
            if (!isValidEmail(email)) {
                alert('Please enter a valid email address.');
                return;
            }

            // Generate Random 4-digit OTP
            generatedOtp = Math.floor(1000 + Math.random() * 9000).toString();

            // Simulate sending OTP
            alert(`Your OTP is: ${generatedOtp}`);

            step1.classList.add('hidden');
            step2.classList.remove('hidden');
        });
    }

    if (verifyOtpBtn) {
        verifyOtpBtn.addEventListener('click', () => {
            const otp = otpInput.value.trim();
            if (otp === generatedOtp) {
                // Success
                loginScreen.style.opacity = '0';
                setTimeout(() => {
                    loginScreen.classList.add('hidden');
                    appContent.classList.remove('hidden');

                    // Trigger animations/cleanup
                    loginScreen.style.display = 'none';
                }, 500); // Fade out duration
            } else {
                alert('Invalid OTP. Please try again.');
            }
        });
    }

    if (backToEmailBtn) {
        backToEmailBtn.addEventListener('click', () => {
            step2.classList.add('hidden');
            step1.classList.remove('hidden');
            otpInput.value = '';
        });
    }


    // --- Find Buddy Logic ---
    const findBuddyBtn = document.getElementById('find-buddy-btn');
    const findBuddyModal = document.getElementById('find-buddy-modal');
    const searchState = document.getElementById('search-state');
    const matchState = document.getElementById('match-state');
    const cancelSearchBtns = document.querySelectorAll('.cancel-search');
    const connectBtn = document.getElementById('connect-btn');

    // Match Data Elements
    const matchImg = document.getElementById('match-img');
    const matchName = document.getElementById('match-name');
    const matchInfo = document.getElementById('match-info');

    // Chat Header Elements to Update
    const buddyNameEl = document.querySelector('.buddy-info h4');
    const buddyImgEl = document.querySelector('.buddy-info img');

    // Dummy Data for Simulation
    const potentialBuddies = [
        { name: 'Jordan Lee', subject: 'Calculus • 4.9', img: 'https://ui-avatars.com/api/?name=Jordan+Lee&background=f472b6&color=fff' },
        { name: 'Casey Smith', subject: 'Physics • 4.8', img: 'https://ui-avatars.com/api/?name=Casey+Smith&background=a78bfa&color=fff' },
        { name: 'Taylor Doe', subject: 'History • 5.0', img: 'https://ui-avatars.com/api/?name=Taylor+Doe&background=2dd4bf&color=fff' }
    ];

    let searchTimeout = null;
    let selectedBuddy = null;

    if (findBuddyBtn && findBuddyModal) {
        findBuddyBtn.addEventListener('click', () => {
            findBuddyModal.classList.remove('hidden');
            searchState.classList.remove('hidden');
            matchState.classList.add('hidden');

            // Simulate API Search Delay
            searchTimeout = setTimeout(() => {
                // Select Random Buddy
                selectedBuddy = potentialBuddies[Math.floor(Math.random() * potentialBuddies.length)];

                // Update UI
                matchImg.src = selectedBuddy.img;
                matchName.textContent = selectedBuddy.name;
                matchInfo.textContent = selectedBuddy.subject;

                // Switch Views
                searchState.classList.add('hidden');
                matchState.classList.remove('hidden');
            }, 3000); // 3 seconds delay
        });

        // Cancel/Close Logic
        cancelSearchBtns.forEach(btn => {
            btn.addEventListener('click', () => {
                clearTimeout(searchTimeout);
                findBuddyModal.classList.add('hidden');
            });
        });

        // Connect Logic
        if (connectBtn) {
            connectBtn.addEventListener('click', () => {
                findBuddyModal.classList.add('hidden');

                // Update Chat Interface
                if (buddyNameEl) buddyNameEl.textContent = selectedBuddy.name + " (Buddy)";
                if (buddyImgEl) buddyImgEl.src = selectedBuddy.img;

                // Navigate to Study Room
                navigateTo('study-room');

                // Optional: Add a system message to chat
                const chatBox = document.getElementById('chat-box');
                if (chatBox) {
                    const msgDiv = document.createElement('div');
                    msgDiv.className = 'message received';
                    msgDiv.innerHTML = `
                        <p><strong>System:</strong> You are now connected with ${selectedBuddy.name}!</p>
                        <span class="time">${new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}</span>
                    `;
                    chatBox.appendChild(msgDiv);
                    chatBox.scrollTop = chatBox.scrollHeight;
                }
            });
        }
    }

});
