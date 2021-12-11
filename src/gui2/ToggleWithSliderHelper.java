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

  public ToggleWithSliderHelper(CheckBox checkbox, Slider slider, Label text, Label percent) {
    this.checkbox = checkbox;
    this.slider = slider;
    this.text = text;
    this.percent = percent;

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

  public void set(ToggleWithSlider toggleWithSlider) {
    checkbox.setSelected(toggleWithSlider.getIsEnabled());
    slider.setValue((double) toggleWithSlider.getSliderValue());
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
