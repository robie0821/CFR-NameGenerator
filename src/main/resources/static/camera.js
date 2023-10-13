// 웹캠 비디오 스트림 가져오기
var video = document.getElementById('video');
var canvas = document.getElementById('canvas');
var context = canvas.getContext('2d');
var photo = document.getElementById('photo');

navigator.mediaDevices.getUserMedia({ video: true })
  .then(function (stream) {
    video.style.cssText = "-moz-transform: scale(-1, 1); \
    -webkit-transform: scale(-1, 1); -o-transform: scale(-1, 1); \
    transform: scale(-1, 1); filter: FlipH;";
    video.srcObject = stream;
  })
  .catch(function (error) {
    console.error('카메라 액세스 오류:', error);
  });

// 버튼을 클릭하여 사진 찍기
document.getElementById('captureButton').addEventListener('click', function () {

  context.scale(-1, 1);
  context.drawImage(video, -canvas.width, 0, canvas.width, canvas.height);
  context.setTransform(1, 0, 0, 1, 0, 0);

  photo.src = canvas.toDataURL('image/jpeg');
});

function posting() {
  const imgBase64 = canvas.toDataURL('image/jpeg', 'image/octet-stream');
  const decodImg = atob(imgBase64.split(',')[1]);

  let array = [];
  for (let i = 0; i < decodImg .length; i++) {
    array.push(decodImg .charCodeAt(i));
  }

  const file = new Blob([new Uint8Array(array)], {type: 'image/jpeg'});
  const fileName = 'canvas_img_' + new Date().getMilliseconds() + '.jpg';
  let formData = new FormData();
  formData.append('uploadFile', file, fileName);

  $.ajax({
    type: "POST",
    url: "/face",
    data: formData,
    cache: false,
    contentType: false,
    processData: false,
    success: (data) => {
      if (data == "happy") {
        location.href="/chat/happy"
      } else if(data == "sad") {
        location.href="/chat/sad"
      } else if(data == "angry") {
        location.href="/chat/angry"
      } else if (data == "no_person") {
        alert('사진에 사람이 없습니다');
      } else if (data == "no_file") {
        alert('사진이 없습니다');
      } else if (data == "error") {
        alert('아무튼 오류');
      }
    }
  });
}

// HTML 요소 찾기
var periodElement = document.getElementById('period');
var remainPeriodElement = document.getElementById('RemainPeriod'); // 시간을 표시할 요소 추가

var period = 3;
var tid = 0;

function chattingClock() {
  period -= 1;
  if (period < 0) {
      if (tid) clearTimeout(tid);

      context.scale(-1, 1);
      context.drawImage(video, -canvas.width, 0, canvas.width, canvas.height);
      context.setTransform(1, 0, 0, 1, 0, 0);

      photo.src = canvas.toDataURL('image/jpeg');
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
