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

        updateChartTheme();
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

  var charts = {};
  var gridLine;
  var titleColor;

  function reloadChart(metricTimeSeries) {
    var width, height, gradient;

    function getGradient(ctx, chartArea) {
      var chartWidth = chartArea.right - chartArea.left;
      var chartHeight = chartArea.bottom - chartArea.top;

      if (gradient === null || width !== chartWidth || height !== chartHeight) {
        width = chartWidth;
        height = chartHeight;
        gradient = ctx.createLinearGradient(0, chartArea.bottom, 0, chartArea.top);
        gradient.addColorStop(0, 'rgba(255, 255, 255, 0)');
        gradient.addColorStop(1, 'rgba(255, 255, 255, 0.4)');
      }

      return gradient;
    }

    var context = document.getElementById('chart-main');
    var rootCanvas = context.getContext('2d');


    // EXTRACT OUT AND PRODUCE PER-AUTHOR TIME SERIES
    var dates = Object.keys(metricTimeSeries);
    var metricsOnDates = Object.values(metricTimeSeries);
    var unit = metricsOnDates[0].unit ?? "count";
    var metricValuesOnDates = [];
    var maxValue = 0;
    metricsOnDates.forEach(function (metric) {
      const value = metric.totalForAllAuthors;
      if (value > maxValue) maxValue = value;
      metricValuesOnDates.push(value);
    });
    dates.push('2021-01-01');
    metricValuesOnDates.push(null);
    dates.push('2023-01-01');
    metricValuesOnDates.push(60);


    var rootChart = new Chart(rootCanvas, {
      type: 'line',
      data: {
        labels: dates,
        datasets: [{
          label: 'Total for all authors',
          data: metricValuesOnDates,
          cubicInterpolationMode: 'monotone',
          tension: 0.4,
          backgroundColor: ['#1f4efa'],
          borderColor: ['#1f4efa'],
          borderWidth: 2
        }
          // , {
          //   label: 'Previous',
          //   data: [20, 36, 16, 45, 29, 32, 10],
          //   cubicInterpolationMode: 'monotone',
          //   tension: 0.4,
          //   backgroundColor: ['rgba(75, 222, 151, 1)'],
          //   borderColor: ['rgba(75, 222, 151, 1)'],
          //   borderWidth: 2
          // }
        ]
      },
      options: {
        scales: {
          y: {
            min: 0,
            max: maxValue + (maxValue * 1.1),
            ticks: {
              stepSize: maxValue / 10,
              callback: function (value) {
                return formatMetric(value, unit);
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
            radius: 2
          }
        },
        plugins: {
          legend: {
            position: 'top',
            align: 'end',
            labels: {
              boxWidth: 8,
              boxHeight: 8,
              usePointStyle: true,
              font: {
                size: 12,
                weight: '500'
              }
            }
          },
          title: {
            display: true,
            text: ['Time Series'],
            align: 'start',
            color: '#171717',
            font: {
              size: 16,
              family: 'Inter',
              weight: '600',
              lineHeight: 1.4
            }
          },
          tooltip: {
            callbacks: {
              label: function (context) {
                return formatMetric(context.parsed.y, unit);
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

    charts.main = rootChart;
    updateChartTheme();
  }

  function updateChartTheme() {
    var darkMode = localStorage.getItem('darkMode');

    if (darkMode === 'enabled') {
      gridLine = '#37374F';
      titleColor = '#EFF0F6';
    } else {
      gridLine = '#EEEEEE';
      titleColor = '#171717';
    }

    if (charts.hasOwnProperty('main')) {
      charts.main.options.scales.x.grid.color = gridLine;
      charts.main.options.plugins.title.color = titleColor;
      charts.main.options.scales.y.ticks.color = titleColor;
      charts.main.options.scales.x.ticks.color = titleColor;
      charts.main.update();
    }
  }

  /* #####  STRICTLY BUSINESS  ##### */

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
        if (data.totalForAllAuthors || data.totalForAllAuthors === 0) {
          holders.authors.total.wrapper.style.display = 'block';
          holders.authors.total.value.textContent = formatMetric(data.totalForAllAuthors, data.unit);
          holders.authors.total.unit.textContent = data.unit;
        } else {
          holders.authors.total.wrapper.style.display = 'none';
        }
        if (data.averagePerAuthor || data.averagePerAuthor === 0) {
          holders.authors.average.wrapper.style.display = 'block';
          holders.authors.average.value.textContent = formatMetric(data.averagePerAuthor, data.unit);
          holders.authors.average.unit.textContent = data.unit;
        } else {
          holders.authors.average.wrapper.style.display = 'none';
        }

        // Reviewers
        if (data.totalForAllReviewers || data.totalForAllReviewers === 0) {
          holders.reviewers.total.wrapper.style.display = 'block';
          holders.reviewers.total.value.textContent = formatMetric(data.totalForAllReviewers, data.unit);
          holders.reviewers.total.unit.textContent = data.unit;
        } else {
          holders.reviewers.total.wrapper.style.display = 'none';
        }
        if (data.averagePerReviewer || data.averagePerReviewer === 0) {
          holders.reviewers.average.wrapper.style.display = 'block';
          holders.reviewers.average.value.textContent = formatMetric(data.averagePerReviewer, data.unit);
          holders.reviewers.average.unit.textContent = data.unit;
        } else {
          holders.reviewers.average.wrapper.style.display = 'none';
        }

        // Code reviews
        if (data.totalForAllCodeReviews || data.totalForAllCodeReviews === 0) {
          holders.codeReviews.total.wrapper.style.display = 'block';
          holders.codeReviews.total.value.textContent = formatMetric(data.totalForAllCodeReviews, data.unit);
          holders.codeReviews.total.unit.textContent = data.unit;
        } else {
          holders.codeReviews.total.wrapper.style.display = 'none';
        }
        if (data.averagePerCodeReview || data.averagePerCodeReview === 0) {
          holders.codeReviews.average.wrapper.style.display = 'block';
          holders.codeReviews.average.value.textContent = formatMetric(data.averagePerCodeReview, data.unit);
          holders.codeReviews.average.unit.textContent = data.unit;
        } else {
          holders.codeReviews.average.wrapper.style.display = 'none';
        }

        // Discussions
        if (data.totalForAllDiscussions || data.totalForAllDiscussions === 0) {
          holders.discussions.total.wrapper.style.display = 'block';
          holders.discussions.total.value.textContent = formatMetric(data.totalForAllDiscussions, data.unit);
          holders.discussions.total.unit.textContent = data.unit;
        } else {
          holders.discussions.total.wrapper.style.display = 'none';
        }
        if (data.averagePerDiscussion || data.averagePerDiscussion === 0) {
          holders.discussions.average.wrapper.style.display = 'block';
          holders.discussions.average.value.textContent = formatMetric(data.averagePerDiscussion, data.unit);
          holders.discussions.average.unit.textContent = data.unit;
        } else {
          holders.discussions.average.wrapper.style.display = 'none';
        }

        // Repositories
        if (data.totalForAllRepositories || data.totalForAllRepositories === 0) {
          holders.repositories.total.wrapper.style.display = 'block';
          holders.repositories.total.value.textContent = formatMetric(data.totalForAllRepositories, data.unit);
          holders.repositories.total.unit.textContent = data.unit;
        } else {
          holders.repositories.total.wrapper.style.display = 'none';
        }
        if (data.averagePerRepository || data.averagePerRepository === 0) {
          holders.repositories.average.wrapper.style.display = 'block';
          holders.repositories.average.value.textContent = formatMetric(data.averagePerRepository, data.unit);
          holders.repositories.average.unit.textContent = data.unit;
        } else {
          holders.repositories.average.wrapper.style.display = 'none';
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
        reloadChart(data);
      })
      .catch(function (error) {
        console.error('Error fetching data: ', error);
        errorHolder.textContent = " · Check console for errors";
      });
  }

});
