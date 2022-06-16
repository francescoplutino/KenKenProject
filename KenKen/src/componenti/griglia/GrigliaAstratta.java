package componenti.griglia;

import java.util.*;

public abstract class GrigliaAstratta implements InterfacciaGriglia {

    private final List<Listener> listeners = new LinkedList<>();

    @Override
    public void addListener(Listener l) {
        if (listeners.contains(l))
            return;
        listeners.add(l);
    }
}
