/* global IntersectionObserver */

(function () {
  var illustration = document.getElementById("illustration");

  new IntersectionObserver(function(entries) {
    if(entries[0].isIntersecting === true) {
      document.body.classList.remove("whoami-header-out")
      document.body.classList.add("whoami-header-in")
      illustration.className = "";
    } else {
      document.body.classList.remove("whoami-header-in")
      document.body.classList.add("whoami-header-out")
    }
  }, { threshold: [0] }).
    observe(document.querySelector("#whoami-header"));

  new IntersectionObserver(function(entries) {
    if(entries[0].isIntersecting === true) {
      document.body.classList.remove("whoami-footer-out")
      document.body.classList.add("whoami-footer-in")
      illustration.className = "";

      var h = illustration.getBoundingClientRect().height;
      illustration.style.bottom = (window.innerHeight - h) + "px"
    } else {
      document.body.classList.remove("whoami-footer-in")
      document.body.classList.add("whoami-footer-out")
      delete illustration.style.bottom;
    }
  }, { threshold: [0] }).
    observe(document.querySelector("#whoami-footer"));

  var observer = new IntersectionObserver(function (entries) {
    var t = entries.filter(function (e) { return e.isIntersecting; }).map(function (e) { return e.target; });
    if (t.length) {
      t[t.length - 1].classList.forEach(function (cn) {
        if (cn.startsWith("scroll--")) {
          illustration.className = cn.substr(8);
        }
      });
    }
  }, { threshold: [0.1] });

  document.querySelectorAll(".whoami-q").forEach(function (e) {
    observer.observe(e);
  });

}());
