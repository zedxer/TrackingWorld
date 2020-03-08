package  com.cornixtech.trackingworld.realm

import com.cornixtech.trackingworld.models.UserDetailModel
import com.cornixtech.trackingworld.models.UserModel
import io.realm.annotations.RealmModule

/**
 * Created by naqi on 04,May,2019
 */

@RealmModule(
    library = true, classes = arrayOf(
        UserModel::class, UserDetailModel::class
    )
)
open class DefaultModule