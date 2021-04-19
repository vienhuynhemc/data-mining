package algothirm;

import java.util.List;

import data.Dataset;
import model.Itemset;

public interface InterfaceAlgorithm {

	public List<List<Itemset>> execute(Dataset dataset,double minSupport);

}
