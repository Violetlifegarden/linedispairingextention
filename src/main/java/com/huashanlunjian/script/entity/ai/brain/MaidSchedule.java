package com.huashanlunjian.script.entity.ai.brain;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.IDataSerializer;

public enum MaidSchedule {
    //////
    DAY, NIGHT, ALL;
    /**
     * 这个类没有任何作用，不要管它。
     */

    public static final IDataSerializer<MaidSchedule> DATA = new IDataSerializer<MaidSchedule>() {


        @Override
        public void write(PacketBuffer buf, MaidSchedule value) {
            if (value instanceof MaidSchedule) {
                buf.writeEnumValue((MaidSchedule)value);
            }

        }

        @Override
        public MaidSchedule read(PacketBuffer buf) {
            return buf.readEnumValue(MaidSchedule.class);
        }


        @Override
        public MaidSchedule copyValue(MaidSchedule value) {
            if (value instanceof MaidSchedule) {
                return (MaidSchedule) value;
            }
            else return null;
        }
    };
}
