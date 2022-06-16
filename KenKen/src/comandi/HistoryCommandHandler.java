package comandi;

import java.util.LinkedList;

public class HistoryCommandHandler implements CommandHandler{


    public enum NonExecutableCommands implements Command {
         REDO, UNDO;
        @Override
        public boolean run() {
            throw new NoSuchMethodError();
        }
    }


    private int maxHistoryLength = 50;

    private final LinkedList<Command> history = new LinkedList<>();

    private final LinkedList<Command> redoList = new LinkedList<>();

    public HistoryCommandHandler() {
        this(50);
    }

    public HistoryCommandHandler(int maxHistoryLength) {
        if (maxHistoryLength < 0)
            throw new IllegalArgumentException();
        this.maxHistoryLength = maxHistoryLength;
    }

    public void handle(Command cmd) {
        if (cmd == NonExecutableCommands.REDO) {
            redo();
            return;
        }
        if (cmd.run()) { // non annulla
            addToHistory(cmd);
        } else { // annulla
            history.clear();
        }
        if (redoList.size() > 0)
            redoList.clear();
    }

    private void redo() {
        if (redoList.size() > 0) {
            Command redoCmd = redoList.removeFirst();
            redoCmd.run();
            history.addFirst(redoCmd);

        }
    }

    private void addToHistory(Command cmd) {
        history.addFirst(cmd);
        if (history.size() > maxHistoryLength) {
            history.removeLast();
        }

    }
}
