package history.storage

import codestats.Repository as DatabaseRepository
import codestats.codestats
import history.storage.mapping.toGeneric
import history.storage.mapping.toStorage
import models.Repository
import models.TeamHistoryConfig

interface RepositoryStorage {

  val teamHistoryConfig: TeamHistoryConfig
  val database: codestats

  fun fetchAllRepositories(): List<Repository> =
    database.repositoryQueries.fetchAll().executeAsList().map(DatabaseRepository::toGeneric)

  fun fetchAllRepositoriesByOwner(): List<Repository> =
    database.repositoryQueries
      .fetchAllByOwner(teamHistoryConfig.owner)
      .executeAsList()
      .map(DatabaseRepository::toGeneric)

  fun fetchOneRepository(name: String): Repository? =
    database.repositoryQueries.fetchOne(teamHistoryConfig.owner, name).executeAsOneOrNull()?.toGeneric()

  fun storeRepository(repository: Repository) = database.repositoryQueries.save(repository.toStorage())

  fun purgeRepositories() = database.repositoryQueries.purge()

  fun deleteOneRepository(name: String) = database.repositoryQueries.deleteOne(teamHistoryConfig.owner, name)

  fun deleteAllRepositoriesByOwner() = database.repositoryQueries.deleteAllByOwner(teamHistoryConfig.owner)

}
