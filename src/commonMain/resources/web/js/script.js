"use strict";

function _createForOfIteratorHelper(o, allowArrayLike) { var it; if (typeof Symbol === "undefined" || o[Symbol.iterator] == null) { if (Array.isArray(o) || (it = _unsupportedIterableToArray(o)) || allowArrayLike && o && typeof o.length === "number") { if (it) o = it; var i = 0; var F = function F() { }; return { s: F, n: function n() { if (i >= o.length) return { done: true }; return { done: false, value: o[i++] }; }, e: function e(_e) { throw _e; }, f: F }; } throw new TypeError("Invalid attempt to iterate non-iterable instance.\nIn order to be iterable, non-array objects must have a [Symbol.iterator]() method."); } var normalCompletion = true, didErr = false, err; return { s: function s() { it = o[Symbol.iterator](); }, n: function n() { var step = it.next(); normalCompletion = step.done; return step; }, e: function e(_e2) { didErr = true; err = _e2; }, f: function f() { try { if (!normalCompletion && it["return"] != null) it["return"](); } finally { if (didErr) throw err; } } }; }

function _unsupportedIterableToArray(o, minLen) { if (!o) return; if (typeof o === "string") return _arrayLikeToArray(o, minLen); var n = Object.prototype.toString.call(o).slice(8, -1); if (n === "Object" && o.constructor) n = o.constructor.name; if (n === "Map" || n === "Set") return Array.from(o); if (n === "Arguments" || /^(?:Ui|I)nt(?:8|16|32)(?:Clamped)?Array$/.test(n)) return _arrayLikeToArray(o, minLen); }

function _arrayLikeToArray(arr, len) { if (len == null || len > arr.length) len = arr.length; for (var i = 0, arr2 = new Array(len); i < len; i++) { arr2[i] = arr[i]; } return arr2; }

document.addEventListener('DOMContentLoaded', function () {
  'use strict';
  /* function testWebP(callback) {
  var webP = new Image();
  webP.onload = webP.onerror = function () {
  callback(webP.height == 2);
  };
  webP.src = "data:image/webp;base64,UklGRjoAAABXRUJQVlA4IC4AAACyAgCdASoCAAIALmk0mk0iIiIiIgBoSygABc6WWgAA/veff/0PP8bA//LwYAAA";
  }
  testWebP(function (support) {
  if (support == true) {
  document.querySelector('body').classList.add('webp');
  }else{
  document.querySelector('body').classList.add('no-webp');
  }
  });*/

  feather.replace();

  (function () {
    var sidebar = document.querySelector('.sidebar'),
      catSubMenu = document.querySelector('.cat-sub-menu'),
      sidebarBtns = document.querySelectorAll('.sidebar-toggle');

    var _iterator = _createForOfIteratorHelper(sidebarBtns),
      _step;

    try {
      for (_iterator.s(); !(_step = _iterator.n()).done;) {
        var sidebarBtn = _step.value;

        if (sidebarBtn && catSubMenu && sidebarBtn) {
          sidebarBtn.addEventListener('click', function () {
            var _iterator2 = _createForOfIteratorHelper(sidebarBtns),
              _step2;

            try {
              for (_iterator2.s(); !(_step2 = _iterator2.n()).done;) {
                var sdbrBtn = _step2.value;
                sdbrBtn.classList.toggle('rotated');
              }
            } catch (err) {
              _iterator2.e(err);
            } finally {
              _iterator2.f();
            }

            sidebar.classList.toggle('hidden');
            catSubMenu.classList.remove('visible');
          });
        }
      }
    } catch (err) {
      _iterator.e(err);
    } finally {
      _iterator.f();
    }
  })();

  (function () {
    var showCatBtns = document.querySelectorAll('.show-cat-btn');

    if (showCatBtns) {
      showCatBtns.forEach(function (showCatBtn) {
        var catSubMenu = showCatBtn.nextElementSibling;
        showCatBtn.addEventListener('click', function (e) {
          e.preventDefault();
          catSubMenu.classList.toggle('visible');
          var catBtnToRotate = document.querySelector('.category__btn');
          catBtnToRotate.classList.toggle('rotated');
        });
      });
    }
  })();

  (function () {
    var showMenu = document.querySelector('.metric-switcher');
    var metricMenu = document.querySelector('.metric-menu');
    var layer = document.querySelector('.layer');

    if (showMenu) {
      showMenu.addEventListener('click', function () {
        metricMenu.classList.add('active');
        layer.classList.add('active');
      });

      if (layer) {
        layer.addEventListener('click', function (e) {
          if (metricMenu.classList.contains('active')) {
            metricMenu.classList.remove('active');
            layer.classList.remove('active');
          }
        });
      }
    }
  })();

  (function () {
    var userDdBtnList = document.querySelectorAll('.dropdown-btn');
    var userDdList = document.querySelectorAll('.users-item-dropdown');
    var layer = document.querySelector('.layer');

    if (userDdList && userDdBtnList) {
      var _iterator3 = _createForOfIteratorHelper(userDdBtnList),
        _step3;

      try {
        for (_iterator3.s(); !(_step3 = _iterator3.n()).done;) {
          var userDdBtn = _step3.value;
          userDdBtn.addEventListener('click', function (e) {
            layer.classList.add('active');

            var _iterator4 = _createForOfIteratorHelper(userDdList),
              _step4;

            try {
              for (_iterator4.s(); !(_step4 = _iterator4.n()).done;) {
                var userDd = _step4.value;

                if (e.currentTarget.nextElementSibling == userDd) {
                  if (userDd.classList.contains('active')) {
                    userDd.classList.remove('active');
                  } else {
                    userDd.classList.add('active');
                  }
                } else {
                  userDd.classList.remove('active');
                }
              }
            } catch (err) {
              _iterator4.e(err);
            } finally {
              _iterator4.f();
            }
          });
        }
      } catch (err) {
        _iterator3.e(err);
      } finally {
        _iterator3.f();
      }
    }

    if (layer) {
      layer.addEventListener('click', function (e) {
        var _iterator5 = _createForOfIteratorHelper(userDdList),
          _step5;

        try {
          for (_iterator5.s(); !(_step5 = _iterator5.n()).done;) {
            var userDd = _step5.value;

            if (userDd.classList.contains('active')) {
              userDd.classList.remove('active');
              layer.classList.remove('active');
            }
          }
        } catch (err) {
          _iterator5.e(err);
        } finally {
          _iterator5.f();
        }
      });
    }
  })();

  (function () {
    Chart.defaults.backgroundColor = '#000';
    var darkMode = localStorage.getItem('darkMode');
    var darkModeToggle = document.querySelector('.theme-switcher');

    var enableDarkMode = function enableDarkMode() {
      document.body.classList.add('darkmode');
      localStorage.setItem('darkMode', 'enabled');
    };

    var disableDarkMode = function disableDarkMode() {
      document.body.classList.remove('darkmode');
      localStorage.setItem('darkMode', null);
    };

    if (darkMode === 'enabled') {
      enableDarkMode();
    }

    if (darkModeToggle) {
      darkModeToggle.addEventListener('click', function () {
        darkMode = localStorage.getItem('darkMode');

        if (darkMode !== 'enabled') {
          enableDarkMode();
        } else {
          disableDarkMode();
        }

        updateChartTheme('chart-per-author');
        updateChartTheme('chart-per-reviewer');
        updateChartTheme('chart-per-code-review');
        updateChartTheme('chart-per-discussion');
        updateChartTheme('chart-per-repository');
      });
    }
  })();

  (function () {
    var checkAll = document.querySelector('.check-all');
    var checkers = document.querySelectorAll('.check');

    if (checkAll && checkers) {
      checkAll.addEventListener('change', function (e) {
        var _iterator6 = _createForOfIteratorHelper(checkers),
          _step6;

        try {
          for (_iterator6.s(); !(_step6 = _iterator6.n()).done;) {
            var checker = _step6.value;

            if (checkAll.checked) {
              checker.checked = true;
              checker.parentElement.parentElement.parentElement.classList.add('active');
            } else {
              checker.checked = false;
              checker.parentElement.parentElement.parentElement.classList.remove('active');
            }
          }
        } catch (err) {
          _iterator6.e(err);
        } finally {
          _iterator6.f();
        }
      });

      var _iterator7 = _createForOfIteratorHelper(checkers),
        _step7;

      try {
        var _loop = function _loop() {
          var checker = _step7.value;
          checker.addEventListener('change', function (e) {
            checker.parentElement.parentElement.parentElement.classList.toggle('active');

            if (!checker.checked) {
              checkAll.checked = false;
            }

            var totalCheckbox = document.querySelectorAll('.users-table .check');
            var totalChecked = document.querySelectorAll('.users-table .check:checked');

            if (totalCheckbox && totalChecked) {
              if (totalCheckbox.length == totalChecked.length) {
                checkAll.checked = true;
              } else {
                checkAll.checked = false;
              }
            }
          });
        };

        for (_iterator7.s(); !(_step7 = _iterator7.n()).done;) {
          _loop();
        }
      } catch (err) {
        _iterator7.e(err);
      } finally {
        _iterator7.f();
      }
    }
  })();

  (function () {
    var checkAll = document.querySelector('.check-all');
    var checkers = document.querySelectorAll('.check');
    var checkedSum = document.querySelector('.checked-sum');

    if (checkedSum && checkAll && checkers) {
      checkAll.addEventListener('change', function (e) {
        var totalChecked = document.querySelectorAll('.users-table .check:checked');
        checkedSum.textContent = totalChecked.length;
      });

      var _iterator8 = _createForOfIteratorHelper(checkers),
        _step8;

      try {
        for (_iterator8.s(); !(_step8 = _iterator8.n()).done;) {
          var checker = _step8.value;
          checker.addEventListener('change', function (e) {
            var totalChecked = document.querySelectorAll('.users-table .check:checked');
            checkedSum.textContent = totalChecked.length;
          });
        }
      } catch (err) {
        _iterator8.e(err);
      } finally {
        _iterator8.f();
      }
    }
  })();


  // CHARTING AND PAGE UPDATES

  String.prototype.hashCode = function () {
    let hash = 0;
    for (let i = 0; i < this.length; i++) {
      const char = this.charCodeAt(i);
      hash = (hash << 5) - hash + char;
    }
    return hash;
  };

  function prepareBreakdownChartData(metricTimeSeries, breakdownSourceSelector, averageSelector) {
    // this finds the dataset for this date, for example a breakdown by author
    // and then collects all the keys from that dataset (i.e. all authors)
    var allDatasourceKeys = [];
    var metricsOnDates = Object.values(metricTimeSeries);
    metricsOnDates.forEach((metricOnDate) => {
      var breakdown = breakdownSourceSelector(metricOnDate);
      Object.keys(breakdown).forEach((key) => { if (!allDatasourceKeys.includes(key)) allDatasourceKeys.push(key); });
    });
    var caseIgnoringComparator = (a, b) => a.localeCompare(b, undefined, { sensitivity: 'base' });
    allDatasourceKeys.sort(caseIgnoringComparator);

    var min = Number.MAX_VALUE;
    var max = Number.MIN_VALUE;
    var unit = "count"; // just a sensible default

    var datasets = {}; // it's going to be a key->[values] map; for example user->[25, 151, null]
    var averages = []; // averages for each date, for example [25.3, 151.5, 204.1]
    allDatasourceKeys.forEach((key) => {
      metricsOnDates.forEach((metricOnDate) => {
        if (metricOnDate.unit) unit = metricOnDate.unit;

        var breakdown = breakdownSourceSelector(metricOnDate);
        var valueOnDate = breakdown[key] || null;
        var valuesTimeSeries = datasets[key] || [];
        valuesTimeSeries.push(valueOnDate);
        datasets[key] = valuesTimeSeries;
        var average = averageSelector(metricOnDate) || null;
        averages.push(average);

        if (valueOnDate !== null && valueOnDate !== undefined && valueOnDate < min) min = valueOnDate;
        if (valueOnDate !== null && valueOnDate !== undefined && valueOnDate > max) max = valueOnDate;
      });
    });

    var dates = Object.keys(metricTimeSeries);
    min = Math.floor(min * 0.9); // 10% padding for the bottom bound
    max = Math.ceil(max * 1.1); // 10% padding for the top bound
    var tickInterval = getIdealTickInterval(min, max);

    return {
      unit: unit,
      tickInterval: tickInterval,
      min: min,
      max: max,
      xValues: dates,
      yValuesMap: datasets,
      averages: averages,
    };
  }

  function getIdealTickInterval(minValue, maxValue) {
    const maxTicks = 10;
    const goodTicks = [
      1, 2, 5,
      10, 15, 20, 25, 50,
      100, 150, 200, 250, 500,
      1000, 1500, 2000, 2500, 5000,
      10000, 15000, 20000, 25000, 50000,
      100000, 150000, 200000, 250000, 500000,
      1000000, 1500000, 2000000, 2500000, 5000000,
      10000000, 15000000, 20000000, 25000000, 50000000,
      100000000, 150000000, 200000000, 250000000, 500000000,
    ];
    const averageTickInterval = (maxValue - minValue) / maxTicks;

    // find the best easy number for tick interval
    let tickInterval = 1;
    for (let i = 0; i < goodTicks.length; i++) {
      const easyNumber = goodTicks[i];
      if (averageTickInterval <= easyNumber) {
        tickInterval = easyNumber;
        break;
      }
    }

    // increase the interval until there are less than maxTicks total
    while ((maxValue - minValue) / tickInterval > maxTicks) {
      tickInterval *= 2;
    }

    return tickInterval;
  }

  function getColorFromKey(key) {
    // Use hash to ensure consistent random variations for the same key
    const randomSeed = Math.abs(key.hashCode());

    // Define acceptable hue ranges (avoiding red)
    const hueRanges = [
      [30, 60],   // Orange
      [240, 300], // Purple
    ];

    const baseHue = randomSeed % 300; // Using a larger range to account for avoided red
    const baseSaturation = (randomSeed * 2654435761) % 31 + 70; // Between 70% and 100%
    const baseLightness = (randomSeed * 2654435761) % 61 + 20; // Between 20% and 80%

    // Calculate hue variation within acceptable ranges
    const hueVariation = (Math.random() * 61 - 30 + randomSeed) % 300;

    // Find the acceptable hue range
    const acceptableRange = hueRanges.find(range => baseHue + hueVariation >= range[0] && baseHue + hueVariation <= range[1]);

    // Calculate final hue within the chosen range
    const hue = (baseHue + hueVariation + (acceptableRange ? acceptableRange[1] : 0) - 30) % 360;
    const saturation = Math.min(Math.max(baseSaturation, 0), 100);
    const lightness = baseLightness;

    return `hsl(${hue}, ${saturation}%, ${lightness}%)`;
  }

  var charts = {};
  var gridLine;
  var titleColor;

  function setUpChart(
    metricTimeSeries,
    breakdownSourceSelector,
    averageSelector,
    chartId,
    chartTitle,
    chartParentId,
    showLegend,
    forceLabels = false,
  ) {
    var chartElement = document.getElementById(chartId);
    var chartParentElement = document.getElementById(chartParentId);
    var canvas = chartElement.getContext('2d');

    var chartData = prepareBreakdownChartData(metricTimeSeries, breakdownSourceSelector, averageSelector);
    var labelToTimeSeriesList = Object.entries(chartData.yValuesMap);
    // usually useless when there are a lot of labels; hide by default when too many
    var disableLabelsDueToSize = labelToTimeSeriesList.length > 15;
    var hasAverages = chartData.averages.some(item => item !== null);
    var datasets = [];

    if (hasAverages) {
      datasets.push({
        label: 'Average',
        data: chartData.averages,
        cubicInterpolationMode: 'monotone',
        tension: 0.4,
        backgroundColor: ['rgba(255, 30, 30, 0.7)'],
        borderColor: ['rgba(255, 30, 30, 0.7)'],
        borderWidth: 3,
        borderDash: [3, 3],
        spanGaps: true,
        hidden: false,
      });
    }

    for (const [sourceKey, timeSeries] of labelToTimeSeriesList) {
      var disableLabelsDueToLackOfData = timeSeries.every(item => item === null);
      datasets.push({
        label: sourceKey,
        data: timeSeries,
        cubicInterpolationMode: 'monotone',
        tension: 0.4,
        backgroundColor: [getColorFromKey(sourceKey)],
        borderColor: [getColorFromKey(sourceKey)],
        borderWidth: 2.5,
        spanGaps: true,
        hidden: !forceLabels && (disableLabelsDueToSize || disableLabelsDueToLackOfData),
      });
    };

    var chartConfig = new Chart(canvas, {
      type: 'line',
      data: {
        labels: chartData.xValues,
        datasets: datasets
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        scales: {
          y: {
            min: chartData.min,
            max: chartData.max,
            ticks: {
              stepSize: chartData.tickInterval,
              callback: function (value) {
                return formatMetric(value, chartData.unit);
              }
            },
            grid: {
              display: false
            }
          },
          x: {
            grid: {
              color: gridLine
            }
          }
        },
        elements: {
          point: {
            radius: 4
          }
        },
        plugins: {
          legend: {
            position: 'top',
            align: 'center',
            labels: {
              boxWidth: 10,
              boxHeight: 10,
              usePointStyle: true,
              font: {
                size: 14,
                weight: '400'
              }
            },
            display: showLegend
          },
          title: {
            display: true,
            text: [chartTitle],
            align: 'center',
            color: '#555555',
            font: {
              size: 16,
              family: 'Inter',
              weight: '400',
              lineHeight: 1.2
            }
          },
          tooltip: {
            callbacks: {
              label: function (context) {
                var pointName = context.dataset.label ? (context.dataset.label + ": ") : '';
                return pointName + formatMetric(context.parsed.y, chartData.unit);
              }
            }
          }
        },
        tooltips: {
          mode: 'index',
          intersect: false
        },
        hover: {
          mode: 'nearest',
          intersect: true
        }
      }
    });

    charts[chartId] = chartConfig;
    updateChartTheme(chartId);

    if (!hasAverages) {
      chartParentElement.style.display = 'none';
    } else {
      chartParentElement.style.display = 'block';
    }
  }

  function updateChartTheme(chartId) {
    var darkMode = localStorage.getItem('darkMode');

    if (darkMode === 'enabled') {
      gridLine = '#37374F';
      titleColor = '#AAAAAA';
    } else {
      gridLine = '#EEEEEE';
      titleColor = '#555555';
    }

    charts[chartId].options.scales.x.grid.color = gridLine;
    charts[chartId].options.plugins.title.color = titleColor;
    charts[chartId].options.scales.y.ticks.color = titleColor;
    charts[chartId].options.scales.x.ticks.color = titleColor;
    charts[chartId].update();
  }

  function transformCamelCase(input) {
    return input.replace(/([a-z])([A-Z])/g, '$1 $2')
      .toLowerCase()
      .replace(/^./, function (str) {
        return str.toUpperCase();
      });
  }

  function getQueryParam(name) {
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    return urlParams.get(name);
  }

  function setSelectedMetric(selectedMetric) {
    const selectedMetricHolder = document.getElementById('selected-metric');
    selectedMetricHolder.innerText = transformCamelCase(selectedMetric);

    updateCards(selectedMetric);
    updateChart(selectedMetric);
  }

  function durationString(milliseconds) {
    if (milliseconds === 0) return '0';

    const seconds = Math.floor(milliseconds / 1000);
    const minutes = Math.floor(seconds / 60);
    const hours = Math.floor(minutes / 60);
    const days = Math.floor(hours / 24);
    const months = Math.floor(days / 30);
    const years = Math.floor(months / 12);

    const result = [];
    if (years > 0) result.push(`${years}Y`);
    if (months > 0) result.push(`${months % 12}M`);
    if (days > 0) result.push(`${days % 30}d`);
    if (hours > 0) result.push(`${hours % 24}h`);
    if (minutes > 0) result.push(`${minutes % 60}m`);

    return result.join(' ').trim();
  }

  function trimFloat(number) {
    if (typeof number === 'number' && !Number.isInteger(number)) return number.toFixed(1);
    return number;
  }

  function formatMetric(count, unit) {
    if (unit === "count") return trimFloat(count);
    if (unit === "time") return durationString(count);
    console.error(`Unknown unit: ${unit}`);
    return count;
  }

  const API_URL_BASE = 'http://localhost:8080';
  const API_URL_METRICS = API_URL_BASE + '/api/metrics';
  const API_URL_COUNTS = API_URL_BASE + '/api/counts';
  const API_URL_TIME_SERIES = API_URL_BASE + '/api/time-series';

  // Populating the drop-down with available metrics
  (function () {
    var metricDropdownWrapper = document.querySelector('.metric-switcher-wrapper .metric-menu');
    var selectedMetric = document.getElementById('selected-metric');

    // Make a GET request to the API endpoint
    fetch(API_URL_METRICS)
      .then(response => response.json())
      .then(function (data) {
        metricDropdownWrapper.innerHTML = '';

        // Iterate over the array of strings and populate the dropdown
        data.forEach(function (metric) {
          var listItem = document.createElement('li');
          var link = document.createElement('a');
          link.href = '?metric=' + metric;
          link.textContent = transformCamelCase(metric);
          listItem.appendChild(link);
          metricDropdownWrapper.appendChild(listItem);
        });

        setSelectedMetric(getQueryParam('metric') || data[0]);
      })
      .catch(function (error) {
        console.error('Error fetching data: ', error);

        selectedMetric.innerText = "Check console for errors";
        selectedMetric.style.color = '#FF4040';
        selectedMetric.style.fontWeight = 'bold';
      });
  })();

  function updateCards(selectedMetric) {
    var holders = {
      error: document.getElementById("main-title-error-box"),
      authors: {
        total: {
          wrapper: document.getElementById("stat-cards-authors-total-wrapper"),
          value: document.getElementById("stat-cards-authors-total-value"),
          unit: document.getElementById("stat-cards-authors-total-unit")
        },
        average: {
          wrapper: document.getElementById("stat-cards-authors-average-wrapper"),
          value: document.getElementById("stat-cards-authors-average-value"),
          unit: document.getElementById("stat-cards-authors-average-unit")
        }
      },
      reviewers: {
        total: {
          wrapper: document.getElementById("stat-cards-reviewers-total-wrapper"),
          value: document.getElementById("stat-cards-reviewers-total-value"),
          unit: document.getElementById("stat-cards-reviewers-total-unit")
        },
        average: {
          wrapper: document.getElementById("stat-cards-reviewers-average-wrapper"),
          value: document.getElementById("stat-cards-reviewers-average-value"),
          unit: document.getElementById("stat-cards-reviewers-average-unit")
        }
      },
      codeReviews: {
        total: {
          wrapper: document.getElementById("stat-cards-code-reviews-total-wrapper"),
          value: document.getElementById("stat-cards-code-reviews-total-value"),
          unit: document.getElementById("stat-cards-code-reviews-total-unit")
        },
        average: {
          wrapper: document.getElementById("stat-cards-code-reviews-average-wrapper"),
          value: document.getElementById("stat-cards-code-reviews-average-value"),
          unit: document.getElementById("stat-cards-code-reviews-average-unit")
        }
      },
      discussions: {
        total: {
          wrapper: document.getElementById("stat-cards-discussions-total-wrapper"),
          value: document.getElementById("stat-cards-discussions-total-value"),
          unit: document.getElementById("stat-cards-discussions-total-unit")
        },
        average: {
          wrapper: document.getElementById("stat-cards-discussions-average-wrapper"),
          value: document.getElementById("stat-cards-discussions-average-value"),
          unit: document.getElementById("stat-cards-discussions-average-unit")
        }
      },
      repositories: {
        total: {
          wrapper: document.getElementById("stat-cards-repositories-total-wrapper"),
          value: document.getElementById("stat-cards-repositories-total-value"),
          unit: document.getElementById("stat-cards-repositories-total-unit")
        },
        average: {
          wrapper: document.getElementById("stat-cards-repositories-average-wrapper"),
          value: document.getElementById("stat-cards-repositories-average-value"),
          unit: document.getElementById("stat-cards-repositories-average-unit")
        }
      }
    }

    fetch(API_URL_COUNTS + '/' + selectedMetric)
      .then(response => response.json())
      .then(function (data) {
        holders.error.textContent = '';

        // Authors
        holders.authors.total.wrapper.style.display = 'block';
        holders.authors.total.value.textContent = formatMetric(data.totalForAllAuthors, data.unit);
        holders.authors.total.unit.textContent = data.unit;
        if (data.averagePerAuthor || data.averagePerAuthor === 0) {
          holders.authors.average.wrapper.style.display = 'block';
          holders.authors.average.value.textContent = formatMetric(data.averagePerAuthor, data.unit);
          holders.authors.average.unit.textContent = data.unit;
        } else {
          holders.authors.average.wrapper.style.display = 'none';
          holders.authors.total.wrapper.style.display = 'none';
        }

        // Reviewers
        holders.reviewers.total.wrapper.style.display = 'block';
        holders.reviewers.total.value.textContent = formatMetric(data.totalForAllReviewers, data.unit);
        holders.reviewers.total.unit.textContent = data.unit;
        if (data.averagePerReviewer || data.averagePerReviewer === 0) {
          holders.reviewers.average.wrapper.style.display = 'block';
          holders.reviewers.average.value.textContent = formatMetric(data.averagePerReviewer, data.unit);
          holders.reviewers.average.unit.textContent = data.unit;
        } else {
          holders.reviewers.average.wrapper.style.display = 'none';
          holders.reviewers.total.wrapper.style.display = 'none';
        }

        // Code reviews
        holders.codeReviews.total.wrapper.style.display = 'block';
        holders.codeReviews.total.value.textContent = formatMetric(data.totalForAllCodeReviews, data.unit);
        holders.codeReviews.total.unit.textContent = data.unit;
        if (data.averagePerCodeReview || data.averagePerCodeReview === 0) {
          holders.codeReviews.average.wrapper.style.display = 'block';
          holders.codeReviews.average.value.textContent = formatMetric(data.averagePerCodeReview, data.unit);
          holders.codeReviews.average.unit.textContent = data.unit;
        } else {
          holders.codeReviews.average.wrapper.style.display = 'none';
          holders.codeReviews.total.wrapper.style.display = 'none';
        }

        // Discussions
        holders.discussions.total.wrapper.style.display = 'block';
        holders.discussions.total.value.textContent = formatMetric(data.totalForAllDiscussions, data.unit);
        holders.discussions.total.unit.textContent = data.unit;
        if (data.averagePerDiscussion || data.averagePerDiscussion === 0) {
          holders.discussions.average.wrapper.style.display = 'block';
          holders.discussions.average.value.textContent = formatMetric(data.averagePerDiscussion, data.unit);
          holders.discussions.average.unit.textContent = data.unit;
        } else {
          holders.discussions.average.wrapper.style.display = 'none';
          holders.discussions.total.wrapper.style.display = 'none';
        }

        // Repositories
        holders.repositories.total.wrapper.style.display = 'block';
        holders.repositories.total.value.textContent = formatMetric(data.totalForAllRepositories, data.unit);
        holders.repositories.total.unit.textContent = data.unit;
        if (data.averagePerRepository || data.averagePerRepository === 0) {
          holders.repositories.average.wrapper.style.display = 'block';
          holders.repositories.average.value.textContent = formatMetric(data.averagePerRepository, data.unit);
          holders.repositories.average.unit.textContent = data.unit;
        } else {
          holders.repositories.average.wrapper.style.display = 'none';
          holders.repositories.total.wrapper.style.display = 'none';
        }
      })
      .catch(function (error) {
        console.error('Error fetching data: ', error);
        holders.error.textContent = " · Check console for errors";
      });
  }

  function updateChart(selectedMetric) {
    var errorHolder = document.getElementById("main-title-error-box");

    fetch(API_URL_TIME_SERIES + '/' + selectedMetric)
      .then(response => response.json())
      .then(function (data) {
        errorHolder.textContent = '';

        var perAuthorSelector = (metricOnDate) => metricOnDate.perAuthor;
        var authorsAverageSelector = (metricOnDate) => metricOnDate.averagePerAuthor;

        var perReviewerSelector = (metricOnDate) => metricOnDate.perReviewer;
        var reviewersAverageSelector = (metricOnDate) => metricOnDate.averagePerReviewer;

        var perCodeReviewSelector = (metricOnDate) => metricOnDate.perCodeReview;
        var codeReviewsAverageSelector = (metricOnDate) => metricOnDate.averagePerCodeReview;

        var perDiscussionSelector = (metricOnDate) => metricOnDate.perDiscussion;
        var discussionsAverageSelector = (metricOnDate) => metricOnDate.averagePerDiscussion;

        var perRepositorySelector = (metricOnDate) => metricOnDate.perRepository;
        var repositoriesAverageSelector = (metricOnDate) => metricOnDate.averagePerRepository;

        setUpChart(data, perAuthorSelector, authorsAverageSelector, 'chart-per-author', 'Authors breakdown', 'chart-per-author-wrapper', true);
        setUpChart(data, perReviewerSelector, reviewersAverageSelector, 'chart-per-reviewer', 'Reviewers breakdown', 'chart-per-reviewer-wrapper', true);
        setUpChart(data, perCodeReviewSelector, codeReviewsAverageSelector, 'chart-per-code-review', 'Code reviews breakdown', 'chart-per-code-review-wrapper', false, true);
        setUpChart(data, perDiscussionSelector, discussionsAverageSelector, 'chart-per-discussion', 'Discussions breakdown', 'chart-per-discussion-wrapper', true);
        setUpChart(data, perRepositorySelector, repositoriesAverageSelector, 'chart-per-repository', 'Repositories breakdown', 'chart-per-repository-wrapper', true);
      })
      .catch(function (error) {
        console.error('Error fetching data: ', error);
        errorHolder.textContent = " · Check console for errors";
      });
  }

});
