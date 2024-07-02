<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>Real-Time Data Dashboard</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
</head>
<body>
<h1>实时传感器数据可视化</h1>
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
        timeStamps[i] = new Date(timeStamps[i]).toLocaleTimeString();
    }
    const humidities = [
        <#list humidities as humidity>
            ${humidity}
            <#if humidity_has_next>
            ,
            </#if>
        </#list>
    ];
    const temperatures = [
      <#list temperatures as temperature>
        ${temperature}
        <#if temperature_has_next>
        ,
        </#if>
      </#list>
    ];
    let maxId = ${maxId}
    const ctx = document.getElementById('sensorChart').getContext('2d');
    const sensorChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels:timeStamps.length ? timeStamps : [],
            datasets: [
                {
                    label: '湿度(百分数)',
                    data: humidities.length ? humidities : [],
                    borderColor: 'rgb(75, 192, 192)',
                    tension: 0.1
                },
                {
                    label: '温度(摄氏度)',
                    data: temperatures.length ? temperatures : [],
                    borderColor: 'rgb(255, 99, 132)',
                    tension: 0.1
                }
            ]
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
        axios.get('/api/data?maxId=' + maxId)
            .then(function (response) {
                const data = response.data;
                const labels = data.map(function (item) {
                    return new Date(item.timeStamp).toLocaleTimeString();
                });
                const humidities = data.map(function (item) {
                    return item.humidity;
                });
                const temperatures = data.map(function (item) {
                    return item.temperature;
                });
                maxId = Math.max(...(data.map(function (item) {
                    return item.id;
                })))
                sensorChart.data.labels.push(...labels);
                sensorChart.data.datasets[0].data.push(...humidities);
                sensorChart.data.datasets[1].data.push(...temperatures);
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
