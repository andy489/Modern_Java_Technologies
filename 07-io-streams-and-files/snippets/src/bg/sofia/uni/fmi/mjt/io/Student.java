package bg.sofia.uni.fmi.mjt.io;

import java.io.Serializable;
import java.util.List;

public record Student(String name, int fn, List<Integer> grades) implements Serializable {
}
