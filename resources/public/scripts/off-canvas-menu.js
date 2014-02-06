(function (bd, showMenu, className, byId) {

  function clickedOn(id, target) {
    if (!target) { return false; }
    if (target.id === id) {
      return true;
    } else {
      return clickedOn(id, target.parentNode);
    }
  }

  function toggleOffCanvasMenu(e) {
    if (bd[className] === showMenu) {
      if (!clickedOn("ocm", e.target)) {
        bd[className] = "";
        return false;
      }
    } else if (clickedOn("ocb", e.target)) {
      bd[className] = showMenu;
      return false;
    }
  }

  document.getElementById("ow").onclick = toggleOffCanvasMenu;
}(document.body, "show-menu", "className"));
