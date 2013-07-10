
package blockplus.transport;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import blockplus.export.PiecesRepresentation;



@SuppressWarnings("serial")
public class HttpServer extends HttpServlet {

    // TODO envoyer les metadata

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write(PiecesRepresentation.getInstance().toJson());
    }

}
