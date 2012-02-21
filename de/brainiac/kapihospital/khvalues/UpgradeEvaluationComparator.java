/**
 * @author Brainiac
 */
package de.brainiac.kapihospital.khvalues;

import java.util.Comparator;

public class UpgradeEvaluationComparator implements Comparator<UpgradeEvaluation> {

    public UpgradeEvaluationComparator() {
    }

    public int compare(UpgradeEvaluation o1, UpgradeEvaluation o2) {
        if (o1.getCostsPerPoint() < o2.getCostsPerPoint())
            return -1;
        if (o1.getCostsPerPoint() > o2.getCostsPerPoint())
            return 1;
        return 0;
    }
}
