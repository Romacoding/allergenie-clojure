function geoFindMe() {
  const status = document.querySelector("#status");

  async function success(position) {
    const latitude = position.coords.latitude;
    const longitude = position.coords.longitude;
    const uri = `https://nominatim.openstreetmap.org/reverse.php?format=jsonv2&lat=${latitude}&lon=${longitude}&zoom=18`
    const result = await fetch(uri)
      .then((response) => response.json())
      .then((data) => data);
    console.log(result);
    const zip = result.address.postcode.slice(0,5);
    status.textContent = `It looks like your zip is: ${zip}`;
  }

  function error() {
    status.textContent = "Unable to retrieve your location";
  }

  if (!navigator.geolocation) {
    status.textContent = "Geolocation is not supported by your browser";
  } else {
    status.textContent = "Locating...";
    navigator.geolocation.getCurrentPosition(success, error);
  }
}

document.addEventListener("DOMContentLoaded", (event) => {
  geoFindMe();
});
