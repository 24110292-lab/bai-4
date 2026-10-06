package vn.iotstar.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.model.User;

public final class SessionSupport {
    public static final String USER_ID = "userId";
    public static final String CURRENT_USER = "currentUser";

    private SessionSupport() {
    }

    public static void signIn(HttpServletRequest request, User user) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            session = request.getSession(true);
        } else {
            request.changeSessionId();
        }
        session.setAttribute(USER_ID, user.getId());
        session.setAttribute(CURRENT_USER, new SessionUser(user));
    }

    public static SessionUser sessionUser(User user) {
        return new SessionUser(user);
    }

    public static final class SessionUser implements java.io.Serializable {
        private static final long serialVersionUID = 1L;

        private final String username;
        private final String fullname;

        private SessionUser(User user) {
            username = user.getUsername();
            fullname = user.getFullname();
        }

        public String getUsername() {
            return username;
        }

        public String getFullname() {
            return fullname;
        }
    }
}
