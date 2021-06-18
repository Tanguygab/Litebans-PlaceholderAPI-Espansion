package io.github.tanguygab.litebansexpansion;


import litebans.api.Database;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerData {

    public final UUID uuid;
    public final Map<DataType,Object> data = new HashMap<>();

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        for (Method m : getClass().getMethods()) {
            try {
                m.invoke(this);
            } catch (Exception ignored) {
                // Not a needed method
            }
        }
    }


    /** Checks **/

    public boolean isBanned() {
        return is(DataType.IS_BANNED,"{bans}",false);
    }
    public boolean isMuted() {
        return is(DataType.IS_MUTED,"{mutes}",false);
    }
    public boolean isTempBanned() {
        return is(DataType.IS_TEMPBANNED, "{bans}", true);
    }
    public boolean isTempMuted() {
        return is(DataType.IS_TEMPMUTED, "{mutes}", true);
    }
    public boolean isWarned() {
        return is(DataType.IS_WARNED, "{warnings}", true);
    }

    private boolean is(DataType datatype, String typename, boolean temp) {
        Utils.checkIs(this,datatype, typename, temp);

        if (data.containsKey(datatype)) return (boolean) data.get(datatype);
        return false;
    }

    /** Counts **/

    public int countBans() {
        return count(DataType.COUNT_BANS,"{bans}",false);
    }
    public int countTempBans() {
        return count(DataType.COUNT_TEMPBANS,"{bans}",true);
    }
    public int countMutes() {
        return count(DataType.COUNT_MUTES,"{mutes}",false);
    }
    public int countTempMutes() {
        return count(DataType.COUNT_TEMPMUTES,"{mutes}",true);
    }
    public int countKicks() {
        return count(DataType.COUNT_KICKS,"{kicks}",false);
    }
    public int countWarns() {
        return count(DataType.COUNT_WARNS,"{warnings}",true);
    }

    private int count(DataType datatype, String typename, boolean temp) {
        Utils.count(this,datatype,typename,temp);

        if (data.containsKey(datatype)) return (int) data.get(datatype);
        return 0;}

    /** Last Reasons **/
    /** Last Dates **/
    /** Expiry **/


}
