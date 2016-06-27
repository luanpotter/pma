package br.com.dextra.pma.model;

import xyz.luan.console.parser.Console;
import lombok.Setter;
import br.com.dextra.pma.date.Date;
import br.com.dextra.pma.date.Time;

/**
 * This represents and incomplete Day (thus, the current one).
 * 
 * It has the last record of the file, that could not be added yet because end
 * time is unknown.
 * 
 * @author luan
 */
public class CurrentDay extends Day {

    private static final long serialVersionUID = -4145000337459303572L;

    @Setter
    private Record lastRecord;

    public CurrentDay(Date date, Time start) {
        super(date, start);
    }

    @Override
    public void printInternal(Console console) {
        super.printInternal(console);
        console.result("Current task: " + lastRecord.getTask() + ", since " + lastRecord.getTime() + " [" + diff() + " minutes ago]");
    }

    private int diff() {
        return Time.now().getDifference(lastRecord.getTime());
    }

    @Override
    public Record lastRecord() {
        return lastRecord;
    }

    @Override
    protected int getTotalTimeInTasks() {
        return super.getTotalTimeInTasks() + diff();
    }
}
