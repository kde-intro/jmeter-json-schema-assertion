package org.apache.jmeter.assertions.gui;

import org.apache.jmeter.assertions.JSONSchemaAssertion;
import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

/**
 * JSON Schema Assertion component GUI.
 */
public class JSONSchemaAssertionGui extends AbstractAssertionGui {
    private static final Logger log = LoggerFactory.getLogger(JSONSchemaAssertionGui.class);
    private static final long serialVersionUID = 241L; // See description there https://jmeter.apache.org/api/serialized-form.html
    private JTextField jsonSchema;

    public JSONSchemaAssertionGui() {
        init();
    }

    /**
     * Return the label to be shown within the JTree-Component.
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
     * Modify a given TestElement to mirror the data in the gui components.
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
     * Implement JMeterGUIComponent.clearGui.
     */
    @Override
    public void clearGui() {
        super.clearGui();

        jsonSchema.setText(""); //$NON-NLS-1$
    }

    /**
     * Configure the GUI from the associated test element.
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
     * Initialize the GUI.
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

        // Doctype
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
