/* Ettersom denne inlines på alle sider, har jeg gjort noen krumspring
 * for å redusere filstørrelsen. Resultatet er at den bare tar 255
 * bytes etter minifisering.
 *
 * "ocm" er off-canvas-menu
 * "ocb" er off-canvas-button
 * "ow" er outer-wrapper - som dekker hele skjermflaten
 *
 * Funksjonaliteten er slik at
 *  - dersom menyen ikke vises, må du trykke på off-canvas-button for å vise den
 *  - dersom menyen vises, kan du trykke hvor som helst utenfor off-canvas-menu for å lukke
 *
 */
(function (bd, showMenu, className) {

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
      bd[className] = "";
      if (!clickedOn("ocm", e.target)) {
        return false;
      }
    } else if (clickedOn("ocb", e.target)) {
      bd[className] = showMenu;
    }
  }

  document.getElementById("ow").onclick = toggleOffCanvasMenu;
}(document.body, "show-menu", "className"));
