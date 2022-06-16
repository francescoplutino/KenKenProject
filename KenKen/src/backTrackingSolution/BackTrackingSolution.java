package backTrackingSolution;


public abstract class BackTrackingSolution<P, S> {

    protected abstract P primoPuntoDecisione();

    protected abstract P prossimoPuntoDecisione(P punto);

    protected abstract P ultimoPuntoDecisione();

    protected abstract S primaScelta(P punto);

    protected abstract S prossimaScelta(S scelta);

    protected abstract S ultimaScelta(P punto);

    protected abstract boolean assegnabile(S scelta, P punto);

    protected abstract void assegna(S scelta, P punto);

    protected abstract void deassegna(S scelta, P punto);

    protected abstract P precedentePuntoDecisione(P punto);

    protected abstract S ultimaSceltaAssegnata(P punto);

    protected abstract void scriviSoluzione(int nSol);

    void risolvi(){
        risolvi(Integer.MAX_VALUE);
    }

    public void risolvi(int solMax) {
        int nr_soluzione = 0;
        P ps = primoPuntoDecisione();
        S c = primaScelta(ps);
        boolean backtrack = false;
        boolean fine = false;
        do {
            while (!backtrack && nr_soluzione < solMax) {
                if (assegnabile(c, ps)) {
                    assegna(c, ps);
                    if (ps.equals(ultimoPuntoDecisione())) {
                        ++nr_soluzione;
                        scriviSoluzione(nr_soluzione);
                        deassegna(c, ps);
                        if (!c.equals(ultimaScelta(ps)))
                            c = prossimaScelta(c);
                        else
                            backtrack = true;
                    } else {
                        ps = prossimoPuntoDecisione(ps);
                        c = primaScelta(ps);
                    }
                } else if (!c.equals(ultimaScelta(ps)))
                    c = prossimaScelta(c);
                else
                    backtrack = true;
            }
            fine = ps.equals(primoPuntoDecisione()) || nr_soluzione == solMax;
            while (backtrack && !fine) {
                ps = precedentePuntoDecisione(ps);
                c = ultimaSceltaAssegnata(ps);
                deassegna(c, ps);
                if (!c.equals(ultimaScelta(ps))) {
                    c = prossimaScelta(c);
                    backtrack = false;
                } else if (ps.equals(primoPuntoDecisione()))
                    fine = true;
            }
        } while (!fine);
    }

}
