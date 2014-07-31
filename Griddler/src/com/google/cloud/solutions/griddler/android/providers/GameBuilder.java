package com.google.cloud.solutions.griddler.android.providers;

import com.google.cloud.solutions.griddler.android.ApplicationSettings;
import com.google.cloud.solutions.griddler.android.models.BoardModel;
import com.google.cloud.solutions.griddler.android.models.GameModel;
import com.google.cloud.solutions.griddler.android.models.GamePlayModel;
import com.google.cloud.solutions.griddler.android.models.LetterModel;
import com.google.cloud.solutions.griddler.android.models.QuestionModel;
import com.google.cloud.solutions.griddler.android.models.WordModel;
import com.google.cloud.solutions.sampleapps.griddler.client.griddler.model.Board;
import com.google.cloud.solutions.sampleapps.griddler.client.griddler.model.Game;
import com.google.cloud.solutions.sampleapps.griddler.client.griddler.model.GamePlay;

import java.util.ArrayList;
import java.util.List;

/**
 * The purpose of this class is to build a {@link GameModel} from cloud endpoint query results of
 * type {@link Game}
 *
 */
public class GameBuilder {

  public static GameModel buildGameModel(ApplicationSettings settings, Game result) {
    GameModel gameModel = null;

    Board board = result.getBoard();

    // ensure that there are the same number of clues as answers
    if (board.getRiddles() != null && board.getAnswers() != null
        && board.getRiddles().size() == board.getAnswers().size()) {

      List<QuestionModel> questions = new ArrayList<QuestionModel>();

      for (int index = 0; index < board.getRiddles().size(); index++) {

        String question = board.getRiddles().get(index);
        String word = board.getAnswers().get(index);

        List<LetterModel> letters = new ArrayList<LetterModel>();
        WordModel wordModel = new WordModel(letters);

        for (Character letter : word.toCharArray()) {
          letters.add(new LetterModel(letter));
        }

        questions.add(new QuestionModel(question, wordModel));
      }

      List<List<Character>> boardDefinition = new ArrayList<List<Character>>();

      if (board.getGridDefinition() != null && board.getGridDefinition().size() > 0) {

        for (String row : board.getGridDefinition()) {

          List<Character> characters = new ArrayList<Character>();

          for (Character letter : row.toCharArray()) {
            characters.add(letter);
          }

          boardDefinition.add(characters);
        }
      }

      GamePlay me = getGamePlayer(settings.getPlayerId(), result.getGamePlays());
      GamePlay otherPlayer = getOtherPlayer(settings.getPlayerId(), result.getGamePlays());

      GamePlayModel currentPlayer = new GamePlayModel(
          me.getPlayer().getId(), me.getPlayer().getNickname(), me.getTimeLeft(),
          me.getCorrectAnswers(), me.getIsWinner());
      GamePlayModel opponent = null;

      if (otherPlayer != null) {
        opponent = new GamePlayModel(
            otherPlayer.getPlayer().getId(), otherPlayer.getPlayer().getNickname(),
            otherPlayer.getTimeLeft(), otherPlayer.getCorrectAnswers(), otherPlayer.getIsWinner());
      }

      Boolean gameFinished = getFinishedGame(result.getGamePlays());

      gameModel = new GameModel(
          result.getId(), new BoardModel(boardDefinition, questions, board.getAllottedTime()),
          currentPlayer, opponent, gameFinished);
    }


    return gameModel;
  }

  private static GamePlay getGamePlayer(Long playerId, List<GamePlay> players) {

    for (GamePlay detail : players) {

      if (detail.getPlayer().getId().compareTo(playerId) == 0) {
        return detail;
      }
    }

    return null;
  }

  private static GamePlay getOtherPlayer(Long playerId, List<GamePlay> players) {

    for (GamePlay detail : players) {
      if (detail.getPlayer().getId().compareTo(playerId) != 0) {
        return detail;
      }
    }

    return null;
  }

  private static boolean getFinishedGame(List<GamePlay> players) {

    for (GamePlay detail : players) {
      if (!detail.getFinished()) {
        return false;
      }
    }

    return true;

  }
}
