package vn.iotstar.tag;

import java.io.IOException;

import org.sitemesh.content.Content;
import org.sitemesh.content.ContentProperty;
import org.sitemesh.webapp.WebAppContext;

import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.PageContext;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;

public class SiteMeshWriteTag extends SimpleTagSupport {
    private String property;

    public void setProperty(String property) {
        this.property = property;
    }

    @Override
    public void doTag() throws JspException, IOException {
        if (!(getJspContext() instanceof PageContext pageContext)) {
            throw new JspException("The SiteMesh write tag requires a JSP PageContext.");
        }

        Object contentValue = pageContext.getRequest().getAttribute(WebAppContext.CONTENT_KEY);
        if (!(contentValue instanceof Content content)) {
            throw new JspException("SiteMesh content is not available to the decorator.");
        }

        ContentProperty contentProperty = content.getExtractedProperties().getChild(property);
        if (contentProperty != null && contentProperty.hasValue()) {
            contentProperty.writeValueTo(pageContext.getOut());
        }
    }
}
