  // HTML 요소 찾기
  var periodElement = document.getElementById('period');
  var remainPeriodElement = document.getElementById('RemainPeriod'); // 시간을 표시할 요소 추가

  var period = 180;

  var tid = 0;

  function chattingClock() {
      period -= 1;
      if (period < 0) {
          if (tid) clearTimeout(tid);
          location.href = 'autoPhoto';
          return;
      }

      strTime = '';
      var minute = parseInt((period / 60) % 60);
      var second = parseInt(period % 60);

      strTime = strTime + minute + '분 ' + second + '초';

      if (remainPeriodElement) {
          remainPeriodElement.innerHTML = strTime;
      }

      if (tid) clearTimeout(tid);
      tid = window.setTimeout(chattingClock, 1000);
  }

  if (period > 0) chattingClock();
