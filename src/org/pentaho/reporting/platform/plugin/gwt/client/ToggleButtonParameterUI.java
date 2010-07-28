package org.pentaho.reporting.platform.plugin.gwt.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ToggleButtonParameterUI extends SimplePanel
{
  private class ToggleButtonParameterClickHandler implements ClickHandler
  {
    private ParameterControllerPanel controller;
    private String parameterName;
    private String choiceValue;
    private boolean multiSelect;
    private List<ToggleButton> buttonList;

    public ToggleButtonParameterClickHandler(final List<ToggleButton> buttonList,
                                             final ParameterControllerPanel controller,
                                             final String parameterName,
                                             final String choiceValue,
                                             final boolean multiSelect)
    {
      this.controller = controller;
      this.parameterName = parameterName;
      this.choiceValue = choiceValue;
      this.multiSelect = multiSelect;
      this.buttonList = buttonList;
    }

    public void onClick(final ClickEvent event)
    {
      final ToggleButton toggleButton = (ToggleButton) event.getSource();

      final ParameterValues parameterValues = controller.getParameterMap();
      // if we are single select buttons, we've got to clear the list
      if (!multiSelect)
      {
        parameterValues.setSelectedValue(parameterName, choiceValue);
        for (final ToggleButton tb : buttonList)
        {
          if (toggleButton != tb)
          {
            tb.setDown(false);
          }
        }
      }
      else
      {
        // remove element if it's already there (prevent dups for checkbox)
        parameterValues.removeSelectedValue(parameterName, choiceValue);
        if (toggleButton.isDown())
        {
          parameterValues.addSelectedValue(parameterName, choiceValue);
        }
      }
      controller.fetchParameters(true);
    }
  }

  public ToggleButtonParameterUI(final ParameterControllerPanel controller, final Parameter parameterElement)
  {
    final String layout = parameterElement.getAttribute("parameter-layout"); //$NON-NLS-1$
    final boolean multiSelect = parameterElement.isMultiSelect(); //$NON-NLS-1$ //$NON-NLS-2$

    // build button ui
    final CellPanel buttonPanel;
    if ("vertical".equals(layout)) //$NON-NLS-1$
    {
      buttonPanel = new VerticalPanel();
    }
    else
    {
      buttonPanel = new HorizontalPanel();
    }
    // need a button list so we can clear other selections for button-single mode
    final List<ToggleButton> buttonList = new ArrayList<ToggleButton>();
    final List<ParameterSelection> choices = parameterElement.getSelections();
    for (int i = 0; i < choices.size(); i++)
    {
      final ParameterSelection choiceElement = choices.get(i);
      final String choiceLabel = choiceElement.getLabel(); //$NON-NLS-1$
      final String choiceValue = choiceElement.getValue(); //$NON-NLS-1$
      final ToggleButton toggleButton = new ToggleButton(choiceLabel);
      toggleButton.setTitle(choiceValue);
      toggleButton.setDown(choiceElement.isSelected());
      buttonList.add(toggleButton);
      toggleButton.addClickHandler(new ToggleButtonParameterClickHandler
          (buttonList, controller, parameterElement.getName(), choiceValue, multiSelect));
      buttonPanel.add(toggleButton);
    }
    setWidget(buttonPanel);
  }

}

// UTC+Parameter=2010-02-09T00:00:00.000+0000
// Client=2010-02-09T00:00:00.0000000
// Server=2010-02-09T00:00:00.000+0000
// GMT=2010-02-09T00:00:00.000-0100
// output-target=table/html;page-mode=page&accepted-page=0&solution=steel-wheels&path=/reports&name=dateparameter.prpt&locale=en_US

// UTC+Parameter=2010-07-28T00:00:00.000+0000&
// Client=2010-07-28T00:00:00.0000000&
// Server=2010-07-28T00:00:00.000+0100&
// GMT=2010-07-28T00:00:00.000-0100&
// output-target=table/html;page-mode=page&accepted-page=0&solution=steel-wheels&path=/reports&name=dateparameter.prpt&locale=en_US
