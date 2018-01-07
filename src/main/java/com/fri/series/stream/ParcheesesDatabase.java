package com.fri.series.stream;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ParcheesesDatabase {
    static Parcheese it1 = new Parcheese(1,1,1);
    static Parcheese it2 = new Parcheese(2,1,2);
    static Parcheese big1 = new Parcheese(3,2,1);
    static Parcheese big2 = new Parcheese(4,2,3);
    private static List<Parcheese> parcheeses = Arrays.asList(it1, it2, big1, big2);

    public static List<Parcheese> getParcheeses() {
        System.out.println("List getted"); return parcheeses;
    }

    public static Parcheese getParcheese(int id) {
        for (Parcheese parcheese : parcheeses) {
            if (parcheese.getId() == (id))
                return parcheese;
        }

        return null;
    }

    public static void addParcheese(Parcheese parcheese) {
        parcheeses.add(parcheese);
    }

    public static void deleteParcheese(int id) {
        for (Parcheese parcheese : parcheeses) {
            if (parcheese.getId() == (id)){
                parcheeses.remove(parcheese);
                break;
            }
        }
    }

    public static List<Parcheese> getParcheeseFromUser(int id) {
        return parcheeses.stream().filter(p -> p.getUserId() == id).collect(Collectors.toList());
    }
}
