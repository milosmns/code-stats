package calculator

import components.data.Repository
import components.metrics.Discussions

class DiscussionsCalculator : GenericLongMetricCalculator<Discussions> {

  override fun calculate(repositories: List<Repository>): Discussions {
    val perUser = repositories
      .flatMap { repository -> repository.discussions }
      .groupBy { discussion -> discussion.author }
      .mapValues { (_, discussions) -> discussions.count().toLong() }

    // this feels like it could be simplified to drop the part after 'toSet'...
    val perReviewer = repositories
      .flatMap { repository -> repository.discussions }
      .flatMap { discussion ->
        // reviewers are commenters who are not the authors of the discussion
        discussion.comments
          .filter { comment -> comment.author != discussion.author }
          .map { comment -> comment.author }
      }
      .toSet()
      .associateWith { reviewer ->
        // we're counting discussions where the reviewer has commented but hasn't started the discussion
        repositories
          .flatMap { repository -> repository.discussions }
          .count { discussion ->
            discussion.comments.any { comment -> comment.author == reviewer } &&
              discussion.author != reviewer
          }
          .toLong()
      }

    val perRepository = repositories
      .associateWith { repository -> repository.discussions.count().toLong() }

    return Discussions(
      perAuthor = perUser,
      perReviewer = perReviewer,
      perRepository = perRepository,
    )
  }

}
