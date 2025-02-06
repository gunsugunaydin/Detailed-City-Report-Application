function triggerRealisticConfetti() {
    confetti({
        particleCount: 150,
        angle: 60,
        spread: 55,
        origin: { x: 0.5, y: 0.5 },
        gravity: 0.3,
        drift: 0.5,
        scalar: 1.2,
        shapes: ['circle', 'square'],
        colors: ['#ff0000', '#00ff00', '#0000ff', '#ff9900'],
        ticks: 200
    });
}

document.getElementById('generateReportButton').addEventListener('click', function () {
    const citiesInput = document.getElementById('cities').value;
    const cities = citiesInput.split(',').map(city => city.trim());
    let downloadLocation = document.getElementById('download-location').value;

    downloadLocation = downloadLocation.replace(/\\/g, '\\\\');

    const errorMessage = document.getElementById('error-message');
    const statusMessage = document.getElementById('statusMessage');
    const generateButton = document.getElementById('generateReportButton');
    const loadingTab = document.getElementById('loadingTab');
    const loadingProgress = document.createElement('div');
    loadingProgress.className = 'loading-progress';

    if (cities.length < 10) {
        errorMessage.style.display = 'block';
        statusMessage.innerHTML = '';
        return;
    } else {
        errorMessage.style.display = 'none';
    }

    generateButton.innerHTML = 'Downloading...';
    generateButton.disabled = true;

    // Show loading bar
    loadingTab.innerHTML = ''; 
    loadingTab.style.display = 'inline-block';
    loadingTab.appendChild(loadingProgress);
    let progress = 0;
    const loadingInterval = setInterval(() => {
        if (progress >= 100) {
            clearInterval(loadingInterval);
        } else {
            progress += 5; 
            loadingProgress.style.width = progress + '%';
        }
    }, 200);

    // API Request
    fetch('/generate-report?cities=' + encodeURIComponent(cities.join(',')) + '&downloadLocation=' + encodeURIComponent(downloadLocation), {
        method: 'POST'
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Server error: ' + response.statusText);
        }
        return response.json();
    })
    .then(data => {
        clearInterval(loadingInterval);

        if (data.error) {
            statusMessage.innerHTML = 'Error: ' + data.error;
            statusMessage.style.color = 'red';
        } else {
            statusMessage.innerHTML = data.message;
            statusMessage.style.color = 'green';
            triggerRealisticConfetti();

            showSuccessAnimation();
        }

        resetButtonState();
    })
    .catch(error => {
        clearInterval(loadingInterval);
        statusMessage.innerHTML = 'An error occurred: ' + error.message;
        statusMessage.style.color = 'red';

        handleErrorScenario();

        resetButtonState();
    });

    function showSuccessAnimation() {
        console.log("Process successfully completed!");
    }

    function handleErrorScenario() {
        alert("Please check your connection or try again.");
    }

    function resetButtonState() {
        generateButton.innerHTML = 'Create Report';
        generateButton.disabled = false;
        loadingTab.style.display = 'none';
    }
});

// Show Info Popup
document.getElementById('infoButton').addEventListener('click', function () {
    document.getElementById('infoPopup').style.display = 'block';
});

// Close Info Popup
document.getElementById('closePopup').addEventListener('click', function () {
    document.getElementById('infoPopup').style.display = 'none';
});