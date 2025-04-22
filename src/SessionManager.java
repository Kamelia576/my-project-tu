import java.util.ArrayList;
import java.util.List;

public class SessionManager {
    private final List<Session> sessions = new ArrayList<>();
    private Session currentSession;
    private int sessionCounter = 0;

    public void createSession(ImageProcessor image) {
        Session session = new Session(++sessionCounter);
        session.setCurrentImage(image);
        sessions.add(session);
        currentSession = session;
        System.out.println("New session started with ID: " + sessionCounter);
    }

    public boolean hasActiveSession() {
        return currentSession != null;
    }

    public Session getCurrentSession() {
        return currentSession;
    }

    public ImageProcessor getCurrentImage() {
        return hasActiveSession() ? currentSession.getCurrentImage() : null;
    }

    public void addImageToCurrentSession(ImageProcessor image) {
        if (hasActiveSession()) {
            currentSession.setCurrentImage(image);
            System.out.println("Image added to current session.");
        } else {
            System.out.println("No active session to add image.");
        }
    }

    public boolean switchSession(int sessionId) {
        for (Session session : sessions) {
            if (session.getSessionId() == sessionId) {
                currentSession = session;
                System.out.println("Switched to session ID: " + sessionId);
                return true;
            }
        }
        System.out.println("Error: No session with ID: " + sessionId);
        return false;
    }

    public void closeSession() {
        if (currentSession != null) {
            System.out.println("Session ID " + currentSession.getSessionId() + " closed.");
            currentSession = null;
        } else {
            System.out.println("No session to close.");
        }
    }

    public List<Session> getAllSessions() {
        return sessions;
    }
}
