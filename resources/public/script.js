function geoFindMe() {
  const status = document.querySelector("#status");
  const find = document.querySelector('#find');

  function success(position) {
    const latitude = position.coords.latitude;
    const longitude = position.coords.longitude;

    console.log(latitude, longitude);
  }

  function error() {
    status.textContent = "Unable to retrieve your location";
  }

  if (!navigator.geolocation) {
    status.textContent = "Geolocation is not supported by your browser";
  } else {
    status.textContent = "Locating…";
    navigator.geolocation.getCurrentPosition(success, error);
  }
}

document.addEventListener("DOMContentLoaded", (event) => {
  document.querySelector('#find').addEventListener('click', geoFindMe);
});
