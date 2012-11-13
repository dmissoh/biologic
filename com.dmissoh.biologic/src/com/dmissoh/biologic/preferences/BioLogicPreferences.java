package com.dmissoh.biologic.preferences;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.core.runtime.Preferences;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.dmissoh.biologic.Activator;
import com.dmissoh.biologic.models.EventConfiguration;

public class BioLogicPreferences {

	public static final String PREF_VALUE_VARIABLES = "biologicPreferences";
	private static final String END_KEY_TAG = "endkey";
	private static final String START_KEY_TAG = "startkey";
	private static final String IS_PUNCTUAL = "ispunctual";
	private static final String NAME_TAG = "name";
	private static final String EVENT_CONFIG_TAG = "event";
	private static final String ROOT_CONFIGURATION_TAG = "configurations";

	private static BioLogicPreferences instance;

	private BioLogicPreferences() {
	}

	public synchronized static BioLogicPreferences getInstance() {
		if (instance == null) {
			instance = new BioLogicPreferences();
		}
		return instance;
	}

	/**
	 * Loads persisted value variables from the preference store. This is done
	 * after loading value variables from the extension point. If a persisted
	 * variable has the same name as a extension contributed variable the
	 * variable's value will be set to the persisted value unless either a) The
	 * persisted value is <code>null</code>, or b) the variable is read-only.
	 */
	public List<EventConfiguration> loadPersistedValueVariables() {
		List<EventConfiguration> eventsConfigurations = new ArrayList<EventConfiguration>();
		String variablesString = Activator.getDefault().getPluginPreferences()
				.getString(PREF_VALUE_VARIABLES);
		if (variablesString.length() == 0) {
			return eventsConfigurations;
		}
		Element root = null;
		Throwable ex = null;
		try {
			ByteArrayInputStream stream = new ByteArrayInputStream(
					variablesString.getBytes("UTF-8")); //$NON-NLS-1$
			DocumentBuilder parser = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			parser.setErrorHandler(new DefaultHandler());
			root = parser.parse(stream).getDocumentElement();
		} catch (UnsupportedEncodingException e) {
			ex = e;
		} catch (ParserConfigurationException e) {
			ex = e;
		} catch (FactoryConfigurationError e) {
			ex = e;
		} catch (SAXException e) {
			ex = e;
		} catch (IOException e) {
			ex = e;
		}
		if (ex != null) {
			return eventsConfigurations;
		}
		if (!root.getNodeName().equals(ROOT_CONFIGURATION_TAG)) {
			return eventsConfigurations;
		}
		NodeList list = root.getChildNodes();
		for (int i = 0, numItems = list.getLength(); i < numItems; i++) {
			Node node = list.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				if (!element.getNodeName().equals(EVENT_CONFIG_TAG)) {
					continue;
				}
				String name = element.getAttribute(NAME_TAG);
				String startKey = element.getAttribute(START_KEY_TAG);
				String endKey = element.getAttribute(END_KEY_TAG);
				EventConfiguration ec = new EventConfiguration(name, startKey
						.charAt(0), endKey.charAt(0));
				ec.setPunctual(Boolean.valueOf(element
						.getAttribute(IS_PUNCTUAL)));
				eventsConfigurations.add(ec);
			}
		}
		return eventsConfigurations;
	}

	/**
	 * Returns a memento representing the value variables currently registered.
	 *
	 * @return memento representing the value variables currently registered
	 * @throws IOException
	 *             if an I/O exception occurs while creating the XML.
	 */
	String getValueVariablesAsXML(List<EventConfiguration> eventConfigs)
			throws IOException, ParserConfigurationException,
			TransformerException {

		Document document = getDocument();
		Element rootElement = document.createElement(ROOT_CONFIGURATION_TAG);
		document.appendChild(rootElement);

		for (EventConfiguration eventConfig : eventConfigs) {
			Element element = document.createElement(EVENT_CONFIG_TAG);
			element.setAttribute(NAME_TAG, eventConfig.getName());
			element.setAttribute(START_KEY_TAG, String.valueOf(eventConfig
					.getStartKey()));
			char endKey = eventConfig.getEndKey();
			element.setAttribute(END_KEY_TAG, String.valueOf(endKey));
			element.setAttribute(IS_PUNCTUAL, String.valueOf(eventConfig
					.isPunctual()));
			rootElement.appendChild(element);
		}
		return serializeDocument(document);
	}

	List<EventConfiguration> getDefault() {
		List<EventConfiguration> eventConfigs = new ArrayList<EventConfiguration>();
		EventConfiguration ecOne = new EventConfiguration("Stand still", 'a', 's');
		EventConfiguration ecTwo = new EventConfiguration("Groom larva", 'y', 'x');
		EventConfiguration ecThree = new EventConfiguration("Carry larva", 'q', 'w');
		EventConfiguration ecFour = new EventConfiguration("Antennate larva", 'p');
		eventConfigs.add(ecOne);
		eventConfigs.add(ecTwo);
		eventConfigs.add(ecThree);
		eventConfigs.add(ecFour);
		return eventConfigs;
	}

	private Document getDocument() throws ParserConfigurationException {
		DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = dfactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		return doc;
	}

	/**
	 * Serializes a XML document into a string - encoded in UTF8 format, with
	 * platform line separators.
	 *
	 * @param doc
	 *            document to serialize
	 * @return the document as a string
	 * @throws TransformerException
	 *             if an unrecoverable error occurs during the serialization
	 * @throws IOException
	 *             if the encoding attempted to be used is not supported
	 */
	private String serializeDocument(Document doc) throws TransformerException,
			UnsupportedEncodingException {
		ByteArrayOutputStream s = new ByteArrayOutputStream();

		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer();
		transformer.setOutputProperty(OutputKeys.METHOD, "xml"); //$NON-NLS-1$
		transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //$NON-NLS-1$

		DOMSource source = new DOMSource(doc);
		StreamResult outputTarget = new StreamResult(s);
		transformer.transform(source, outputTarget);

		return s.toString("UTF8"); //$NON-NLS-1$
	}

	/**
	 * Saves the value variables currently registered in the preference store.
	 */
	synchronized void storeValueVariables(List<EventConfiguration> eventConfigs) {
		Preferences prefs = Activator.getDefault().getPluginPreferences();
		String variableString = ""; //$NON-NLS-1$
		try {
			variableString = getValueVariablesAsXML(eventConfigs);
		} catch (IOException e) {
			return;
		} catch (ParserConfigurationException e) {
			return;
		} catch (TransformerException e) {
			return;
		}
		prefs.setValue(PREF_VALUE_VARIABLES, variableString);
	}

	void performDefaults() {
		List<EventConfiguration> eventConfigs = getDefault();
		storeValueVariables(eventConfigs);
	}

}
