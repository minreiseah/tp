package seedu.address.ui.appointment;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.logic.Logic;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.person.Person;
import seedu.address.ui.MainWindow;
import seedu.address.ui.UiPart;

/**
 * Panel containing the list of appointments.
 * Each appointment is also displayed along with some fields of its associated
 * person.
 */
public class AppointmentListPanel extends UiPart<Region> {

    private static final String FXML = "appointmentView/AppointmentListPanel.fxml";

    private final Logic logic;

    private final MainWindow mainWindow;

    @FXML
    private ListView<Appointment> appointmentListView;

    /**
     * Creates a {@code PersonListPanel} with the given {@code ObservableList}.
     * Logic is injected because every update to {@code ObservableList<Appointment>}
     * requires a corresponding call to get the updated list's appointments'
     * associated Persons.
     * This is because {@code ObservableList<Appointment>} does not have
     * any relationship with {@code Person}.
     * {@code appointmentListView} also updates whenever changes are made to
     * {@code personList}.
     */
    public AppointmentListPanel(Logic logic, ObservableList<Person> personList,
            ObservableList<Appointment> appointmentList, MainWindow mainWindow) {
        super(FXML);
        this.logic = logic;
        this.mainWindow = mainWindow;
        appointmentListView.setItems(appointmentList);
        appointmentListView.setCellFactory(listView -> new AppointmentListViewCell());

        personList.addListener((ListChangeListener<Person>) change -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasPermutated() || change.wasRemoved() || change.wasReplaced()
                        || change.wasUpdated()) {
                    appointmentListView.refresh();
                }
            }
        });
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Appointment}
     * using a {@code AppointmentCard}.
     */
    class AppointmentListViewCell extends ListCell<Appointment> {
        @Override
        protected void updateItem(Appointment appointment, boolean empty) {
            super.updateItem(appointment, empty);

            if (empty || appointment == null) {
                setGraphic(null);
                setText(null);
            } else {
                Person person = logic.getPersonById(appointment.getPersonId());
                setGraphic(new AppointmentCard(appointment, person, getIndex() + 1, mainWindow).getRoot());
            }
        }
    }
}
