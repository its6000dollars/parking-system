var sections = {
    vehicleSelection: document.getElementById('vehicleSelection'),
    licensePlateForm: document.getElementById('licensePlateForm'),
    result: document.getElementById('result'),
    complate: document.getElementById('complate')
};

function showSection(sectionToShow) {
    Object.values(sections).forEach(section => {
        section.classList.add('hidden');
    });
    sections[sectionToShow].classList.remove('hidden');
}

function selectVehicle(vehicleType) {
    document.getElementById('vehicleType').value = vehicleType;
    showSection('licensePlateForm');
}

function backToVehicleSelection() {
    document.getElementById('licensePlate').value = '';
    showSection('vehicleSelection');
}

async function handleSubmit(event) {
    event.preventDefault();
    const licensePlate = document.getElementById('licensePlate').value.trim();
    const vehicleType = document.getElementById('vehicleType').value;

    if (!licensePlate) {
        alert('請輸入車牌號碼');
        return;
    }

    try {
        const response = await fetch('/calculate', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                vehicleType: vehicleType,
                licensePlate: licensePlate
            })
        });

        if (!response.ok) {
            throw new Error('繳費資訊查詢失敗');
        }

        const result = await response.json();

        document.getElementById('licensePlateResult').textContent = result.licensePlate;
        document.getElementById('entryTimeResult').textContent = result.entryTime;
        document.getElementById('exitTimeResult').textContent = result.exitTime;
        document.getElementById('parkingTimeResult').textContent = result.parkingTime;
        document.getElementById('feeResult').textContent = "$" + result.fee;

        showSection('result');
    } catch (error) {
        alert("查無車輛資訊");
    }
}

async function payFee() {
    const licensePlate = document.getElementById('licensePlateResult').textContent;
    const vehicleType = document.getElementById('vehicleType').value;

    try {
        const response = await fetch('/pay', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                vehicleType: vehicleType,
                licensePlate: licensePlate
            })
        });

        if (!response.ok) {
            throw new Error('繳費失敗');
        }

        showSection('complate');
        setTimeout(() => {
            location.reload(); 
        }, 2000);
    } catch (error) {
        alert(error.message);
    }
}


