package com.example.hw2.data;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class HighScoreDao_Impl implements HighScoreDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<HighScore> __insertionAdapterOfHighScore;

  private final SharedSQLiteStatement __preparedStmtOfClear;

  public HighScoreDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfHighScore = new EntityInsertionAdapter<HighScore>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `high_scores` (`id`,`score`,`distance`,`coins`,`latitude`,`longitude`,`hasLocation`,`timestamp`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final HighScore entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getScore());
        statement.bindLong(3, entity.getDistance());
        statement.bindLong(4, entity.getCoins());
        statement.bindDouble(5, entity.getLatitude());
        statement.bindDouble(6, entity.getLongitude());
        final int _tmp = entity.getHasLocation() ? 1 : 0;
        statement.bindLong(7, _tmp);
        statement.bindLong(8, entity.getTimestamp());
      }
    };
    this.__preparedStmtOfClear = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM high_scores";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final HighScore score, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfHighScore.insertAndReturnId(score);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object clear(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClear.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClear.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<HighScore>> topTen() {
    final String _sql = "SELECT * FROM high_scores ORDER BY score DESC, timestamp DESC LIMIT 10";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"high_scores"}, new Callable<List<HighScore>>() {
      @Override
      @NonNull
      public List<HighScore> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfScore = CursorUtil.getColumnIndexOrThrow(_cursor, "score");
          final int _cursorIndexOfDistance = CursorUtil.getColumnIndexOrThrow(_cursor, "distance");
          final int _cursorIndexOfCoins = CursorUtil.getColumnIndexOrThrow(_cursor, "coins");
          final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
          final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
          final int _cursorIndexOfHasLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "hasLocation");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<HighScore> _result = new ArrayList<HighScore>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HighScore _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final int _tmpScore;
            _tmpScore = _cursor.getInt(_cursorIndexOfScore);
            final int _tmpDistance;
            _tmpDistance = _cursor.getInt(_cursorIndexOfDistance);
            final int _tmpCoins;
            _tmpCoins = _cursor.getInt(_cursorIndexOfCoins);
            final double _tmpLatitude;
            _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
            final double _tmpLongitude;
            _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
            final boolean _tmpHasLocation;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfHasLocation);
            _tmpHasLocation = _tmp != 0;
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new HighScore(_tmpId,_tmpScore,_tmpDistance,_tmpCoins,_tmpLatitude,_tmpLongitude,_tmpHasLocation,_tmpTimestamp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
