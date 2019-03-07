package eu.thesystems.cloudnet.discord.utility;
/*
 * Created by Mc_Ruben on 07.03.2019
 */

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Getter
public class MapBuilder<K, V> {

    private Map<K, V> map;

    public MapBuilder(Map<K, V> map) {
        this.map = map;
    }

    public MapBuilder() {
        this(new HashMap<>());
    }

    public MapBuilder<K, V> put(K k, V v) {
        this.map.put(k, v);
        return this;
    }

}
