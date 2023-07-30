package history.storage.sqlite

import codestats.codestats
import components.data.TeamHistoryConfig
import components.data.User
import history.storage.mapping.toStorage

interface UserStorage {

  val teamHistoryConfig: TeamHistoryConfig
  val database: codestats

  fun fetchAllUsers(): List<User> = database.userQueries.fetchAll().executeAsList().map(::User)

  fun fetchOneUser(login: String): User? = database.userQueries.fetchOne(login).executeAsOneOrNull()?.let(::User)

  fun storeUser(user: User) = database.userQueries.save(user.toStorage())

  fun purgeUsers() = database.userQueries.purge()

  fun deleteOneUser(login: String) = database.userQueries.deleteOne(login)

}
