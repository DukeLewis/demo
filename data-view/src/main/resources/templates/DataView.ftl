<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>Real-Time Data Dashboard</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
    <style>
        button {
            display: inline-block;
            padding: 10px 20px;
            font-size: 12px;
            font-weight: bold;
            color: #fff;
            background-color: #409eff;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            text-align: center;
            text-decoration: none;
            outline: none;
            transition: background-color 0.3s;
            float: right;
            margin-right: 12px;
        }

        /* 按钮悬停效果 */
        button:hover {
            background-color: #66b1ff; /* 悬停时的背景色 */
        }

        /* 按钮按下效果 */
        button:active {
            background-color: #3a8ee6; /* 按下时的背景色 */
        }
        .canvas-container {
            margin-top: 24px;
            display: flex;
            width: 100%;
            height: 100%;
        }
        canvas {
            flex: 1;
            height: 100%;
            max-width: 50%;
        }
    </style>
</head>
<body>
<h1>实时传感器数据可视化</h1>
<div style="height: 35px">
    <button onclick="publishMsg(1)">获取空气湿度和空气温度</button>
    <button onclick="publishMsg(2)">执行一次浇水程序</button>
    <button onclick="publishMsg(3)">关闭水泵</button>
    <button onclick="publishMsg(4)">开启水泵</button>
</div>
<div class="canvas-container">
    <canvas id="sensorChart" width="800" height="320"></canvas>
    <canvas id="sensorChart2" width="800" height="320"></canvas>
</div>
<script>
    const publishMsg = (operation) => {
        const operationMap = new Map([
            [1, 'get_airmsg'],
            [2, 'water_pro'],
            [3, 'close'],
            [4, 'open']
        ]);
        axios.get('/api/' + operationMap.get(operation))
            .catch(err => {
                console.error('Error fetching data:', err);
            })
    }
    let timeStamps = [
        <#if timeStamps?has_content>
            <#list timeStamps as timeStamp>
            ${timeStamp}
            <#if timeStamp_has_next>
            ,
            </#if>
            </#list>
        </#if>
    ];
    let timeStamps2 = [
        <#if timeStamps2?has_content>
            <#list timeStamps2 as timeStamp>
            ${timeStamp}
            <#if timeStamp_has_next>
            ,
            </#if>
            </#list>
        </#if>
    ];
    for (let i = 0; i < timeStamps.length; i++) {
        timeStamps[i] = new Date(timeStamps[i]).toLocaleTimeString();
    }
    for (let i = 0; i < timeStamps2.length; i++) {
        timeStamps2[i] = new Date(timeStamps2[i]).toLocaleTimeString();
    }
    const humidities = [
        <#if humidities?has_content>
            <#list humidities as humidity>
            ${humidity}
            <#if humidity_has_next>
            ,
            </#if>
            </#list>
        </#if>
    ];
    const humidities2 = [
        <#if humidities2?has_content>
        <#list humidities2 as humidity>
        ${humidity}
        <#if humidity_has_next>
        ,
        </#if>
        </#list>
        </#if>
    ];
    const temperatures = [
      <#if temperatures?has_content>
        <#list temperatures as temperature>
        ${temperature}
        <#if temperature_has_next>
        ,
        </#if>
        </#list>
      </#if>
    ];
    let maxId = ${maxId}
    let maxId2 = #{maxId2}
    const ctx = document.getElementById('sensorChart').getContext('2d');
    const sensorChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels:timeStamps.length ? timeStamps : [],
            datasets: [
                {
                    label: '空气湿度(百分数)',
                    data: humidities.length ? humidities : [],
                    borderColor: 'rgb(75, 192, 192)',
                    tension: 0.1
                },
                {
                    label: '空气温度(摄氏度)',
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
    const ctx2 = document.getElementById('sensorChart2').getContext('2d');
    const sensorChart2 = new Chart(ctx2, {
        type: 'line',
        data: {
            labels:timeStamps2.length ? timeStamps2 : [],
            datasets: [
                {
                    label: '土壤湿度(百分数)',
                    data: humidities2.length ? humidities2 : [],
                    borderColor: 'rgb(75, 192, 192)',
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
                if (!data || data.length === 0) {
                    return;
                }
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
        axios.get('/api/data2?maxId2=' + maxId2)
            .then(function (response) {
                const data = response.data;
                if (!data || data.length === 0) {
                    return;
                }
                const labels = data.map(function (item) {
                    return new Date(item.timeStamp).toLocaleTimeString();
                });
                const humidities = data.map(function (item) {
                    return item.humidity;
                });
                maxId2 = Math.max(...(data.map(function (item) {
                    return item.id;
                })))
                sensorChart2.data.labels.push(...labels);
                sensorChart2.data.datasets[0].data.push(...humidities);
                sensorChart2.update();
            })
            .catch(function (error) {
                console.error('Error fetching data:', error);
            });
    }

    setInterval(fetchData, 1000); // 每1秒刷新一次数据
</script>
</body>
</html>
