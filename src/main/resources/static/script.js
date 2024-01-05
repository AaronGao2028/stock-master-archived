function toggleSectorAllocationChart(){
    const sectorAllocationChart = document.getElementById("sectorAllocationChart");

    if (sectorAllocationChart.style.display == "none"){
        sectorAllocationChart.style.display = "block";
        document.getElementById("toggleSectorButton").innerHTML = "Hide Sector Allocation Chart";
    } else {
        sectorAllocationChart.style.display = "none";
        document.getElementById("toggleSectorButton").innerHTML = "Show Sector Allocation Chart";
    }
}

function toggleMarketCapAllocationChart(){
    const marketCapAllocationChart = document.getElementById("marketCapAllocationChart");

    if (marketCapAllocationChart.style.display == "none"){
        marketCapAllocationChart.style.display = "block";
        document.getElementById("toggleMarketCapButton").innerHTML = "Hide Market Cap Allocation Chart";
    } else {
        marketCapAllocationChart.style.display = "none";
        document.getElementById("toggleMarketCapButton").innerHTML = "Show Market Cap Allocation Chart";
    }
}

function toggleDividendAllocationChart(){
    const dividendAllocationChart = document.getElementById("dividendAllocationChart");

    if (dividendAllocationChart.style.display == "none"){
        dividendAllocationChart.style.display = "block";
        document.getElementById("toggleDividendButton").innerHTML = "Hide Dividend Allocation Chart";
    } else {
        dividendAllocationChart.style.display = "none";
        document.getElementById("toggleDividendButton").innerHTML = "Show Dividend Allocation Chart";
    }
}

function toggleStockList(){
    const stockList = document.getElementById("stockList");

    if (stockList.style.display == "none"){
        stockList.style.display = "block";
        document.getElementById("toggleStockListButton").innerHTML = "Hide Stock Portfolio";
    } else {
        stockList.style.display = "none";
        document.getElementById("toggleStockListButton").innerHTML = "Show Stock Portfolio";
    }
}


// ... Other functions ...
document.getElementById("sectorForm").addEventListener('submit', function(event) {
    event.preventDefault(); // Prevent the default form submission

    postStocks(event); // Call the postStocks function passing the event object
});

function postStocks(event) {
    event.preventDefault(); // Prevent the form from submitting

    var form = document.getElementById("sectorForm");
    const formData = new FormData(form);

    fetch('http://localhost:8080/submit-sectors', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        if (data.hasOwnProperty('goodStockAllocationList') && data.hasOwnProperty('sectorAllocationList') && data.hasOwnProperty("marketCapAllocationList")) {
            const goodStockAllocationList = data.goodStockAllocationList;
            const sectorAllocationList = data.sectorAllocationList;
            const marketCapAllocationList = data.marketCapAllocationList;
            const dividendAllocationList = data.dividendAllocationList;

            // Update the sector allocation pie chart
            updatePieChart(sectorAllocationList);

            //Update the market cap allocation pie chart
            updateMarketCapChart (marketCapAllocationList);

            //Update the dividend allocation pie chart
            updateDividendChart(dividendAllocationList);

            // Update the stock table
            updateStockTable(goodStockAllocationList);
        } else {
            document.getElementById("dataContainer").innerHTML = "An error occurred while fetching data. V1";
        }
    })
    .catch(error => {
        console.error('Error:', error);
        document.getElementById("dataContainer").innerHTML = "An error occurred while fetching data. V2";
    });
}


let currentPieChart = null;

function updatePieChart(sectorAllocationList) {
    document.getElementById("toggleSector").style.display = "block";
    document.getElementById('sectorPieChartTitle').innerHTML = 'Sector Allocation Chart';       

    const sectorNames = sectorAllocationList.map(item => item.sectorName); // Use 'sectorName'
    const sectorAllocations = sectorAllocationList.map(item => item.allocationPercentage); // Use 'allocationPercentage'

    const ctx = document.getElementById('sectorPieChart').getContext('2d');

    // Destroy the current pie chart instance if it exists
    if (currentPieChart) {
        currentPieChart.destroy();
    }

    currentPieChart = new Chart(ctx, {
        type: 'pie',
        data: {
            labels: sectorNames,
            datasets: [{
                data: sectorAllocations,
                backgroundColor: [
                '#FF6384', // Red
                '#36A2EB', // Blue
                '#FFCE56', // Yellow
                '#4CAF50', // Green
                '#E91E63', // Pink
                '#9C27B0', // Purple
                '#FF5722', // Orange
                '#607D8B', // Gray
                '#8BC34A', // Light Green
                '#FF9800', // Amber
                '#3F51B5', // Indigo
                ],
            }],
        },
        options: {
            // Customize chart options as needed

            title: {
                display: true,
                text: 'Sector Allocation Percentage', // Your desired title here
                fontSize: 16, // Font size of the title
                fontColor: '#333', // Color of the title text
                position: 'top', // Position of the title: 'top', 'bottom', 'left', 'right'
            },
        },
    });
}

let marketCapAllocationPieChart = null;

function updateMarketCapChart(marketCapAllocationList) {
    document.getElementById("marketCapAllocationPieChartTitle").innerHTML = "Market Cap Allocation Chart";
    document.getElementById("toggleMarketCap").style.display = "block";

    const marketCapName = marketCapAllocationList.map(item => item.marketCapName); // Use 'sectorName'
    const marketCapAllocation = marketCapAllocationList.map(item => item.allocationPercentage); // Use 'allocationPercentage'

    const ctx = document.getElementById('marketCapAllocationPieChart').getContext('2d');

    // Destroy the current pie chart instance if it exists
    if (marketCapAllocationPieChart) {
        marketCapAllocationPieChart.destroy();
    }

    marketCapAllocationPieChart = new Chart(ctx, {
        type: 'pie',
        data: {
            labels: marketCapName,
            datasets: [{
                data: marketCapAllocation,
                backgroundColor: [
                '#FF6384', // Red
                '#36A2EB', // Blue
                '#FFCE56', // Yellow
                '#4CAF50', // Green
                '#E91E63', // Pink
                '#9C27B0', // Purple
                '#FF5722', // Orange
                '#607D8B', // Gray
                '#8BC34A', // Light Green
                '#FF9800', // Amber
                '#3F51B5', // Indigo
                ],
            }],
        },
        options: {
            title: {
                display: true,
                text: 'Sector Allocation Percentage', // Your desired title here
                fontSize: 18,
                fontColor: '#333',
            },
        // Customize other chart options as needed
        },
    });
}

let dividendAllocationPieChart = null;

function updateDividendChart(dividendAllocationList) {
    document.getElementById("dividendAllocationPieChartTitle").innerHTML = "Dividend Allocation Chart";
    document.getElementById("toggleDividend").style.display = "block";

    const dividendName = dividendAllocationList.map(item => item.dividendName); // Use 'sectorName'
    const dividendAllocation = dividendAllocationList.map(item => item.allocationPercentage); // Use 'allocationPercentage'

    const ctx = document.getElementById('dividendAllocationPieChart').getContext('2d');

    // Destroy the current pie chart instance if it exists
    if (dividendAllocationPieChart) {
        dividendAllocationPieChart.destroy();
    }

    dividendAllocationPieChart = new Chart(ctx, {
        type: 'pie',
        data: {
            labels: dividendName,
            datasets: [{
                data: dividendAllocation,
                backgroundColor: [
                '#FF6384', // Red
                '#36A2EB', // Blue
                '#FFCE56', // Yellow
                '#4CAF50', // Green
                '#E91E63', // Pink
                '#9C27B0', // Purple
                '#FF5722', // Orange
                '#607D8B', // Gray
                '#8BC34A', // Light Green
                '#FF9800', // Amber
                '#3F51B5', // Indigo
                ],
            }],
        },
        options: {
            title: {
                display: true,
                text: 'Sector Allocation Percentage', // Your desired title here
                fontSize: 18,
                fontColor: '#333',
            },
        // Customize other chart options as needed
        },
    });
}

function updateStockTable(goodStockAllocationList) {
    document.getElementById("toggleStockList").style.display = "block";
    
    let markup = `
        <table class="stock-table">
            <tr>
                <th>Ticker</th>
                <th>Name</th>
                <th>Price</th>
                <th>Market Cap</th>
                <th>Dividend</th>
                <th>Sector</th>
                <th>Allocation</th>
            </tr>
    `;

    goodStockAllocationList.forEach(stockAllocation => {
        const stock = stockAllocation.stock;
        markup += `
            <tr>
                <td>${stock.ticker}</td>
                <td>${stock.name}</td>
                <td>$${stock.price}</td>
                <td>${stock.marketCap}B</td>
                <td>${stock.dividend}%</td>
                <td>${stock.sector}</td>
                <td>${stockAllocation.allocation}%</td>
            </tr>
        `;
    });

    markup += `</table>`;

    document.getElementById("dataContainer").innerHTML = markup;
}




function getStocks(){
    fetch('http://localhost:8080/api/v1/stock')
    .then(res => {
        return  res.json();
    })
    .then(data => {
        let markup = "";
        data.forEach(user => {
            markup += `<li>${user.name}</li>`;

            document.getElementById("poster").innerHTML = markup;
        });
    })
    .catch(error => console.log(error));
}

function processName() {
    var form = document.getElementById("nameCollector");
    var firstName=form.elements[0].value;
    var lastName=form.elements[1].value;
    var text="Hello " + firstName + " " + lastName + "!";

    document.getElementById("d0Result").innerHTML=text;
    document.getElementById("d1").style.display="block";
}