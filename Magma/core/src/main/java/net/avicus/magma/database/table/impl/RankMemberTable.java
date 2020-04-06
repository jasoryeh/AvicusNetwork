package net.avicus.magma.database.table.impl;

import net.avicus.libraries.quest.database.Database;
import net.avicus.libraries.quest.model.Table;
import net.avicus.magma.database.model.impl.RankMember;
import net.avicus.magma.database.model.impl.User;

import java.util.Date;
import java.util.List;

public class RankMemberTable extends Table<RankMember> {

    public RankMemberTable(Database database, String name, Class<RankMember> model) {
        super(database, name, model);
    }

    public void delete(RankMember membership) {
        delete().where("id", membership.getId()).execute();
    }

    public void setExpiration(RankMember membership, Date date) {
        update().where("id", membership.getId()).set("expires_at", date).execute();
    }

    public List<RankMember> findByUser(User user) {
        return select().where("member_id", user.getId()).execute();
    }
}
