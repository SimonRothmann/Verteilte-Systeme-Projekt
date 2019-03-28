package dhbwka.wwi.vertsys.javaee.filmsortierung.common.web;

import dhbwka.wwi.vertsys.javaee.filmsortierung.common.ejb.UserBean;
import dhbwka.wwi.vertsys.javaee.filmsortierung.common.ejb.ValidationBean;
import dhbwka.wwi.vertsys.javaee.filmsortierung.common.jpa.User;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 *
 * @author simon
 */
@WebServlet(urlPatterns = {"/app/changeData/"})
public class ChangeServlet extends HttpServlet {

    @EJB
    ValidationBean validationBean;

    @EJB
    UserBean userBean;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        String vorname = request.getParameter("change_vorname");
        String nachname = request.getParameter("change_nachname");
        String passwort1 = request.getParameter("change_new_passwort1");
        String passwort2 = request.getParameter("change_new_passwort2");
        String altesPasswort = request.getParameter("change_old_password");

        User user = userBean.getCurrentUser();
        if (!vorname.isBlank()) {
            user.setForename(vorname);
        }
        if (!nachname.isBlank()) {
            user.setSurname(nachname);
        }

        if (passwort1.isEmpty() && passwort2.isEmpty() && altesPasswort.isEmpty()) {
            userBean.update(user);
            response.sendRedirect(request.getContextPath() + "/app/changeData/");
            return;
        }

        List<String> errors = this.validationBean.validate(user);
        if (!user.checkPassword(altesPasswort)) {
            errors.add("Das alte Passwort stimmt nicht.");
        }

        this.validationBean.validate(passwort1, errors);

        if (passwort1.length() < 6) {
            errors.add("Das neue Passwort muss zwischen sechs und 64 Zeichen lang sein.");
        }

        if (!passwort1.isEmpty() && !passwort2.isEmpty() && !passwort1.equals(passwort2)) {
            errors.add("Die beiden neuen Passwörter stimmen nicht überein.");
        }

        // Neuen Eintrag speichern
        if (errors.isEmpty()) {
            try {
                this.userBean.changePassword(user, altesPasswort, passwort1);
            } catch (UserBean.InvalidCredentialsException ex) {
                Logger.getLogger(ChangeServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.userBean.update(user);
            session.removeAttribute("change_form");
            response.sendRedirect(request.getContextPath() + "/app/changeData/");
        } else {
            // Fehler: Formuler erneut anzeigen
            FormValues formValues = new FormValues();
            formValues.setValues(request.getParameterMap());
            formValues.setErrors(errors);

            session.setAttribute("change_form", formValues);

            response.sendRedirect(request.getRequestURI());
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = userBean.getCurrentUser();

        // Verfügbare Spezies und Stati für die Suchfelder ermitteln
        request.setAttribute("current_user", user);

        // Anzuzeigende Aufgaben suchen
        request.setAttribute("change_vorname", user.getForename());
        request.setAttribute("change_nachname", user.getSurname());

        // Anfrage an die JSP weiterleiten
        request.getRequestDispatcher("/WEB-INF/login/changeData.jsp").forward(request, response);
    }
}
