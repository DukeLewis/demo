<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>Real-Time Data Dashboard</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
</head>
<body>
<h1>Real-Time Sensor Data Visualization</h1>
<canvas id="sensorChart" width="800" height="350"></canvas>
<script>
    let timeStamps = [
        <#list timeStamps as timeStamp>
            ${timeStamp}
            <#if timeStamp_has_next>
            ,
            </#if>
        </#list>
    ];
    for (let i = 0; i < timeStamps.length; i++) {
        timeStamps[i] = new Date(timeStamps[i]).toLocaleTimeString();;
    }
    const payloads = [
        <#list payloads as payload>
            ${payload}
            <#if payload_has_next>
            ,
            </#if>
        </#list>
    ];
    const ctx = document.getElementById('sensorChart').getContext('2d');
    const sensorChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels:timeStamps.length ? timeStamps : [],
            datasets: [{
                label: 'Sensor Value',
                data: payloads.length ? payloads : [],
                borderColor: 'rgb(75, 192, 192)',
                tension: 0.1
            }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });

    function fetchData() {
        axios.get('/api/data')
            .then(function (response) {
                const data = response.data;
                const labels = data.map(function (item) {
                    return new Date(item.timeStamp).toLocaleTimeString();
                });
                const values = data.map(function (item) {
                    return item.payload;
                });
                sensorChart.data.labels = labels;
                sensorChart.data.datasets[0].data = values;
                sensorChart.update();
            })
            .catch(function (error) {
                console.error('Error fetching data:', error);
            });
    }

    setInterval(fetchData, 1000); // 每1秒刷新一次数据
</script>
</body>
</html>
