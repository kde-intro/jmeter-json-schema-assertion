package org.apache.jmeter.assertions.gui;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.jmeter.assertions.JSONSchemaAssertion;

/**
 * JSON Schema Assertion component GUI.
 * Created by Denis Krasilnikov (kde-intro) on 06.03.2017.
 *
 */
public class JSONSchemaAssertionGUI extends AbstractAssertionGui {
    // class attributes
    private static final Logger log = LoggerFactory.getLogger(JSONSchemaAssertionGUI.class);
    private static final long serialVersionUID = 241L; // See description there https://jmeter.apache.org/api/serialized-form.html
    private JTextField jsonSchema;

    /**
     * The constructor.
     */
    public JSONSchemaAssertionGUI() {
        init();
    }

    /**
     * Returns the label to be shown within the JTree-Component.
     */
    @Override
    public String getLabelResource() {
        return "jsonschema_assertion_title"; //$NON-NLS-1$
    }

    /**
     * Create Test Element.
     */
    @Override
    public TestElement createTestElement() {
        log.debug("JSONSchemaAssertionGui.createTestElement() called");
        JSONSchemaAssertion el = new JSONSchemaAssertion();
        modifyTestElement(el);
        return el;
    }

    /**
     * Modifies a given TestElement to mirror the data in the gui components.
     *
     * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement).
     */
    @Override
    public void modifyTestElement(TestElement inElement) {
        log.debug("JSONSchemaAssertionGui.modifyTestElement() called");
        configureTestElement(inElement);
        ((JSONSchemaAssertion) inElement).setJsdFileName(jsonSchema.getText());
    }

    /**
     * Implements JMeterGUIComponent.clearGui.
     */
    @Override
    public void clearGui() {
        super.clearGui();

        jsonSchema.setText(""); //$NON-NLS-1$
    }

    /**
     * Configures the GUI from the associated test element.
     *
     * @param el -
     *            the test element (should be JSONSchemaAssertion).
     */
    @Override
    public void configure(TestElement el) {
        super.configure(el);
        JSONSchemaAssertion assertion = (JSONSchemaAssertion) el;
        jsonSchema.setText(assertion.getJsdFileName());
    }

    /**
     * Inits GUI.
     */
    private void init() {
        setLayout(new BorderLayout(0, 10));
        setBorder(makeBorder());

        add(makeTitlePanel(), BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // USER_INPUT
        VerticalPanel assertionPanel = new VerticalPanel();
        assertionPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                "JSON Schema"));

        // doctype
        HorizontalPanel jsonSchemaPanel = new HorizontalPanel();

        jsonSchemaPanel.add(new JLabel(JMeterUtils.getResString(
                "jsonschema_assertion_label"))); //$NON-NLS-1$

        jsonSchema = new JTextField(26);
        jsonSchemaPanel.add(jsonSchema);

        assertionPanel.add(jsonSchemaPanel);

        mainPanel.add(assertionPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }
}
