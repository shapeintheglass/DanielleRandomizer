package gui2;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import proto.RandomizerSettings.ToggleWithSlider;

public class ToggleWithSliderHelper {
  private CheckBox checkbox;
  private Slider slider;
  private Label text;
  private Label percent;
  private double defaultValue;

  public ToggleWithSliderHelper(CheckBox checkbox, Slider slider, Label text, Label percent, double defaultValue) {
    this.checkbox = checkbox;
    this.slider = slider;
    this.text = text;
    this.percent = percent;
    this.defaultValue = defaultValue;
    set(false, defaultValue);

    update();

    checkbox.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        if (event.getSource() instanceof CheckBox) {
          update();
        }
      }
    });

    text.textProperty()
        .bind(Bindings.format("%.0f", slider.valueProperty()));
  }
  
  public double getDefaultValue() {
    return defaultValue;
  }

  public void set(ToggleWithSlider toggleWithSlider) {
    set(toggleWithSlider.getIsEnabled(), (double) toggleWithSlider.getSliderValue());
  }

  public void set(boolean isEnabled, double value) {
    checkbox.setSelected(isEnabled);
    slider.setValue(value);
    update();
  }

  public boolean isEnabled() {
    return checkbox.isSelected();
  }

  public int getValue() {
    return (int) slider.getValue();
  }

  private void update() {
    boolean isEnabled = checkbox.isSelected();
    toggle(isEnabled);
  }

  private void toggle(boolean isEnabled) {
    slider.setDisable(!isEnabled);
    text.setDisable(!isEnabled);
    percent.setDisable(!isEnabled);
  }
}
