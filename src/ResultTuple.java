import edu.rit.io.InStream;
import edu.rit.io.OutStream;
import edu.rit.pj2.Tuple;

import java.io.IOException;

public class ResultTuple extends Tuple {
    public int rank;     // Worker task rank
    public int step;     // Time step number
    public int lb;       // Lower bound zombie index

    public ResultTuple() {
    }

    public ResultTuple(int rank, int step, int lb) {
        this.rank = rank;
        this.step = step;
        this.lb = lb;
    }


    public void writeOut(OutStream out) throws IOException {
        out.writeInt(rank);
        out.writeInt(step);
        out.writeInt(lb);
    }

    public void readIn(InStream in) throws IOException {
        rank = in.readInt();
        step = in.readInt();
        lb = in.readInt();
    }

    public void combine(ResultTuple resultTuple){
        // TODO: Combine the result.
    }

}
