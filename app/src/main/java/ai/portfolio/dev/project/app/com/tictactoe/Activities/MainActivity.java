package ai.portfolio.dev.project.app.com.tictactoe.Activities;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.games.GamesCallbackStatusCodes;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.OnRealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateCallback;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

import ai.portfolio.dev.project.app.com.tictactoe.BuildConfig;
import ai.portfolio.dev.project.app.com.tictactoe.Fragments.TicTacToeFragment;
import ai.portfolio.dev.project.app.com.tictactoe.Loaders.ImageLoader;
import ai.portfolio.dev.project.app.com.tictactoe.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private static final int RC_SIGN_IN = 1;
    private static final String TAG = "DEBUG_TAG";
    private static final long ANY_ROLE = 0;
    private static final int GOOGLE_SINGLE_SIGN_IN = 2;
    private RoomConfig mJoinedRoomConfig;
    private MediaPlayer mMediaPlayer;
    private InterstitialAd mInterstitialAd, mInterstitialAdMultiplayer;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadADs();

        mMediaPlayer = MediaPlayer.create(this,R.raw.game_home);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

         navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState != null) {

        } else {
            Button game = (Button) this.findViewById(R.id.btn_ai);
            game.setAnimation(bounceAnimation(8000));
            game.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   mInterstitialAd.show();
                }
            });

            Button online = (Button) this.findViewById(R.id.btn_multiplayer);
            online.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mInterstitialAdMultiplayer.show();
                }
            });

            animateBackground();

        }

        updateColorNavigationView();
    }

    /**
     * Adds AD listener for adMob game play ads for homescreen.
     */
    private void loadADs() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAdMultiplayer=new InterstitialAd(this);

        if(BuildConfig.DEBUG){
            mInterstitialAd.setAdUnitId(getString(R.string.video_test_ad_id));
            mInterstitialAdMultiplayer.setAdUnitId(getString(R.string.video_test_ad_id));
        }else{
            mInterstitialAd.setAdUnitId(getString(R.string.ad_mob_video_home_screen));
            mInterstitialAdMultiplayer.setAdUnitId(getString(R.string.ad_mob_video_multiplayer));
        }
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAdMultiplayer.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                startSignInIntent();
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });
        mInterstitialAdMultiplayer.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                try {
                    startQuickGame(ANY_ROLE);
                }catch (Exception e){
                    Toast.makeText(MainActivity.this,"Feature coming soon!",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });
    }

    private void animateBackground() {
        final SurfaceView surfaceView = (SurfaceView)this.findViewById(R.id.surfaceView);


    }
    private void startSignInIntent() {
        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN).requestScopes(Games.SCOPE_GAMES_LITE)
                .requestEmail().requestProfile()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent, GOOGLE_SINGLE_SIGN_IN);
    }

 /*   private void signInSilently() {
        final GoogleSignInClient signInClient = GoogleSignIn.getClient(this,
                GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
        signInClient.silentSignIn().addOnCompleteListener(this,
                new OnCompleteListener<GoogleSignInAccount>() {
                    @Override
                    public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                        if (task.isSuccessful()) {
                            // The signed in account is stored in the task's result.

                            GoogleSignInAccount signedInAccount = task.getResult();
                            showAlertDialog("Welcome " + signedInAccount.getDisplayName());
                        } else {
                            // Player will need to sign-in explicitly using via UI
                            startSignInIntent();
                        }
                    }
                });
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        //signInSilently();
        if(mMediaPlayer!=null)mMediaPlayer.start();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            Fragment fragment = getSupportFragmentManager().findFragmentByTag(TicTacToeFragment.FRAGMENT_TAG);
            if (fragment != null) { // and then you define a method allowBackPressed with the logic to allow back pressed or not
                ((TicTacToeFragment) fragment).backButtonPressed();//remove fragment
                resetAudio();
            }else{
                super.onBackPressed();
            }
        }
    }

    private void resetAudio() {
        //Log.e("AUDIO","media player: "+mMediaPlayer);
        if(this.mMediaPlayer!=null){
            this.mMediaPlayer.start();
        }else {
            this.mMediaPlayer = MediaPlayer.create(this, R.raw.game_home);
            this.mMediaPlayer.setLooping(true);
            this.mMediaPlayer.start();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        //return true;
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.action_settings:
                break;
                default:
                    break;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_sign_out:
                signOut();
                break;
            case R.id.nav_settings:
                if(isInGamePlay()){
                    displayExitGameDialog().show();
                }else{
                openSettingsActivity();
                }
                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private AlertDialog displayExitGameDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_title_gameplay_ext)).setMessage(getString(R.string.dialog_message_settings_exit)).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openSettingsActivity();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog alertDialog= builder.create();
        return alertDialog;
    }

    /**
     * Will check if game is already started.
     * @return
     */
    private boolean isInGamePlay() {
       return getSupportFragmentManager().findFragmentByTag(TicTacToeFragment.FRAGMENT_TAG)!=null;//fragment is started and running
    }

    private void openSettingsActivity() {
        this.startActivity(new Intent(this, SettingsActivity.class));
    }

    /**
     * Uodate main content.xml to game fragment
     * @param containerViewId
     * @param fragment
     * @param fragmentTag
     */
    protected void displayFragment(@IdRes int containerViewId,
                               @NonNull Fragment fragment,
                               @NonNull String fragmentTag) {
        android.support.v4.app.FragmentManager fragMgr = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction xact = fragMgr.beginTransaction();
        if (null == fragMgr.findFragmentByTag(fragmentTag)) {
            xact.add (containerViewId,fragment,fragmentTag)
                    .commit();
        }
       cleanUpHomeScreen();//cleans anything having to do with main activity home screen, ie, audio
    }

    /**
     * @param containerViewId
     * @param fragmentTag
     */
    protected void replaceFragment(@IdRes int containerViewId,
                                   @NonNull String fragmentTag) {
        Fragment frag = getSupportFragmentManager().findFragmentByTag(TicTacToeFragment.FRAGMENT_TAG);
        if(frag!=null) {
            getSupportFragmentManager()
                    .beginTransaction().replace(containerViewId, frag).commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        switch (requestCode){
            case GOOGLE_SINGLE_SIGN_IN:
                if (result.isSuccess()) {
                    // The signed in account is stored in the result.
                    GoogleSignInAccount signedInAccount = result.getSignInAccount();
                    beginGame(signedInAccount);
                } else {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    handleSignInResult(task);

                }
                break;
            case RC_SIGN_IN:
                if (result.isSuccess()) {
                    // The signed in account is stored in the result.
                    GoogleSignInAccount signedInAccount = result.getSignInAccount();
                    startQuickGame(ANY_ROLE);
                } else {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    handleSignInResult(task);

                }
                break;
            case RC_WAITING_ROOM:
                // Look for finishing the waiting room from code, for example if a
                // "start game" message is received.  In this case, ignore the result.
                if (mWaitingRoomFinishedFromCode) {
                    return;
                }

                if (resultCode == Activity.RESULT_OK) {
                    // Start the game!
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    // Waiting room was dismissed with the back button. The meaning of this
                    // action is up to the game. You may choose to leave the room and cancel the
                    // match, or do something else like minimize the waiting room and
                    // continue to connect in the background.

                    // in this example, we take the simple approach and just leave the room:
                    Games.getRealTimeMultiplayerClient(thisActivity,
                            GoogleSignIn.getLastSignedInAccount(this))
                            .leave(mJoinedRoomConfig, mRoom.getRoomId());
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                } else if (resultCode == GamesActivityResultCodes.RESULT_LEFT_ROOM) {
                    // player wants to leave the room.
                    Games.getRealTimeMultiplayerClient(thisActivity,
                            GoogleSignIn.getLastSignedInAccount(this))
                            .leave(mJoinedRoomConfig, mRoom.getRoomId());
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }
                break;
                default:
                    break;
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            showAlertDialog("SIGN IN WAS SUCCESSFUL! Welcome " + account.getDisplayName());
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            e.printStackTrace();
            //showAlertDialog("signInResult:failed code=" + e.getStatusCode()+" ... msg: "+e.getMessage());
        }
    }

    private void updateColorNavigationView(){
        View view = navigationView.getHeaderView(0);
        SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(this);//get persistent preferences from FragmentPreferences

        //int colorPOne = spref.getInt(getString(R.string.pref_player_one_color_key),getResources().getColor(R.color.colorPlayerOne));
        int colorPTwo = spref.getInt(getString(R.string.pref_player_two_color_key),getResources().getColor(R.color.colorPlayerTwo));


        ((TextView)view.findViewById(R.id.score_opponent_tv)).setTextColor(colorPTwo);
        ((TextView)view.findViewById(R.id.textView2)).setTextColor(colorPTwo);
    }
    private void beginGame(final GoogleSignInAccount account) {

        View view = navigationView.getHeaderView(0);
        final ImageView imageView = (ImageView)view.findViewById(R.id.imageView);
        getSupportLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<Bitmap>() {
            @NonNull
            @Override
            public Loader<Bitmap> onCreateLoader(int id, @Nullable Bundle args) {
                return new ImageLoader(MainActivity.this,account.getPhotoUrl().toString());
            }

            @Override
            public void onLoadFinished(@NonNull Loader<Bitmap> loader, Bitmap data) {
                    imageView.setImageBitmap(data);
            }

            @Override
            public void onLoaderReset(@NonNull Loader<Bitmap> loader) {

            }
        });
        TicTacToeFragment frag = TicTacToeFragment.newInstance(account);
        displayFragment(R.id.main,frag,TicTacToeFragment.FRAGMENT_TAG);
        cleanUpHomeScreen();
    }

    public void showToast(String txt) {
        Toast.makeText(MainActivity.this, txt, Toast.LENGTH_LONG).show();
    }

    public void showAlertDialog(String msg) {
        new AlertDialog.Builder(this).setMessage(msg)
                .setNeutralButton(android.R.string.ok, null).show();
    }

    private void startQuickGame(long role) {
        // auto-match criteria to invite one random automatch opponent.
        // You can also specify more opponents (up to 3).

        Bundle autoMatchCriteria = RoomConfig.createAutoMatchCriteria(1, 1,role);
        // build the room config:
        RoomConfig roomConfig =
                RoomConfig.builder(mRoomUpdateCallback)
                        .setOnMessageReceivedListener(mMessageReceivedHandler)
                        .setRoomStatusUpdateCallback(mRoomStatusCallbackHandler)
                        .setAutoMatchCriteria(autoMatchCriteria)
                        .build();

        // prevent screen from sleeping during handshake
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Save the roomConfig so we can use it if we call leave().
        mJoinedRoomConfig = roomConfig;
        Games.getRealTimeMultiplayerClient(this, GoogleSignIn.getLastSignedInAccount(this))
                    .create(roomConfig);
    }

    private RoomUpdateCallback mRoomUpdateCallback = new RoomUpdateCallback() {
        @Override
        public void onRoomCreated(int code, @Nullable Room room) {
            // Update UI and internal state based on room updates.
            if (code == GamesCallbackStatusCodes.OK && room != null) {
                Log.d(TAG, "Room " + room.getRoomId() + " created.");
            } else {
                Log.w(TAG, "Error creating room: " + code);
                // let screen go to sleep
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            }
        }

        @Override
        public void onJoinedRoom(int code, @Nullable Room room) {
            // Update UI and internal state based on room updates.
            if (code == GamesCallbackStatusCodes.OK && room != null) {
                Log.d(TAG, "Room " + room.getRoomId() + " joined.");
            } else {
                Log.w(TAG, "Error joining room: " + code);
                // let screen go to sleep
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            }
        }

        @Override
        public void onLeftRoom(int code, @NonNull String roomId) {
            Log.d(TAG, "Left room" + roomId);
        }

        @Override
        public void onRoomConnected(int code, @Nullable Room room) {
            if (code == GamesCallbackStatusCodes.OK && room != null) {
                Log.d(TAG, "Room " + room.getRoomId() + " connected.");
            } else {
                Log.w(TAG, "Error connecting to room: " + code);
                // let screen go to sleep
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            }
        }
    };


    // are we already playing?
    boolean mPlaying = false;

    // at least 2 players required for our game
    final static int MIN_PLAYERS = 2;

    // returns whether there are enough players to start the game
    boolean shouldStartGame(Room room) {
        int connectedPlayers = 0;
        for (Participant p : room.getParticipants()) {
            if (p.isConnectedToRoom()) {
                ++connectedPlayers;
            }
        }
        return connectedPlayers >= MIN_PLAYERS;
    }

    // Returns whether the room is in a state where the game should be canceled.
    boolean shouldCancelGame(Room room) {
        // TODO: Your game-specific cancellation logic here. For example, you might decide to
        // cancel the game if enough people have declined the invitation or left the room.
        // You can check a participant's status with Participant.getStatus().
        // (Also, your UI should have a Cancel button that cancels the game too)
        return false;
    }

    private Activity thisActivity = this;
    private Room mRoom;
    private String mMyParticipantId;
    private RoomStatusUpdateCallback mRoomStatusCallbackHandler = new RoomStatusUpdateCallback() {
        @Override
        public void onRoomConnecting(@Nullable Room room) {
            // Update the UI status since we are in the process of connecting to a specific room.
        }

        @Override
        public void onRoomAutoMatching(@Nullable Room room) {
            // Update the UI status since we are in the process of matching other players.
        }

        @Override
        public void onPeerInvitedToRoom(@Nullable Room room, @NonNull List<String> list) {
            // Update the UI status since we are in the process of matching other players.
        }

        @Override
        public void onPeerDeclined(@Nullable Room room, @NonNull List<String> list) {
            // Peer declined invitation, see if game should be canceled
            if (!mPlaying && shouldCancelGame(room)) {
                Games.getRealTimeMultiplayerClient(thisActivity,
                        GoogleSignIn.getLastSignedInAccount(thisActivity))
                        .leave(mJoinedRoomConfig, room.getRoomId());
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        }

        @Override
        public void onPeerJoined(@Nullable Room room, @NonNull List<String> list) {
            // Update UI status indicating new players have joined!
        }

        @Override
        public void onPeerLeft(@Nullable Room room, @NonNull List<String> list) {
            // Peer left, see if game should be canceled.
            if (!mPlaying && shouldCancelGame(room)) {
                Games.getRealTimeMultiplayerClient(thisActivity,
                        GoogleSignIn.getLastSignedInAccount(thisActivity))
                        .leave(mJoinedRoomConfig, room.getRoomId());
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        }

        @Override
        public void onConnectedToRoom(@Nullable Room room) {
            // Connected to room, record the room Id.
            mRoom = room;
            Games.getPlayersClient(thisActivity, GoogleSignIn.getLastSignedInAccount(thisActivity))
                    .getCurrentPlayerId().addOnSuccessListener(new OnSuccessListener<String>() {
                @Override
                public void onSuccess(String playerId) {
                    mMyParticipantId = mRoom.getParticipantId(playerId);
                }
            });
        }

        @Override
        public void onDisconnectedFromRoom(@Nullable Room room) {
            // This usually happens due to a network error, leave the game.
            Games.getRealTimeMultiplayerClient(thisActivity, GoogleSignIn.getLastSignedInAccount(thisActivity))
                    .leave(mJoinedRoomConfig, room.getRoomId());
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            // show error message and return to main screen
            mRoom = null;
            mJoinedRoomConfig = null;
        }

        @Override
        public void onPeersConnected(@Nullable Room room, @NonNull List<String> list) {
            if (mPlaying) {
                // add new player to an ongoing game
            } else if (shouldStartGame(room)) {
                // start game!
            }
        }

        @Override
        public void onPeersDisconnected(@Nullable Room room, @NonNull List<String> list) {
            if (mPlaying) {
                // do game-specific handling of this -- remove player's avatar
                // from the screen, etc. If not enough players are left for
                // the game to go on, end the game and leave the room.
            } else if (shouldCancelGame(room)) {
                // cancel the game
                Games.getRealTimeMultiplayerClient(thisActivity,
                        GoogleSignIn.getLastSignedInAccount(thisActivity))
                        .leave(mJoinedRoomConfig, room.getRoomId());
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        }

        @Override
        public void onP2PConnected(@NonNull String participantId) {
            // Update status due to new peer to peer connection.
        }

        @Override
        public void onP2PDisconnected(@NonNull String participantId) {
            // Update status due to  peer to peer connection being disconnected.
        }
    };

    private static final int RC_WAITING_ROOM = 9007;

    private void showWaitingRoom(Room room, int maxPlayersToStartGame) {
        Games.getRealTimeMultiplayerClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .getWaitingRoomIntent(room, maxPlayersToStartGame)
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, RC_WAITING_ROOM);
                    }
                });
    }

    private OnRealTimeMessageReceivedListener mMessageReceivedHandler =
            new OnRealTimeMessageReceivedListener() {
                @Override
                public void onRealTimeMessageReceived(@NonNull RealTimeMessage realTimeMessage) {
                    // Handle messages received here.
                    byte[] message = realTimeMessage.getMessageData();
                    // process message contents...
                }
            };


    boolean mWaitingRoomFinishedFromCode = false;

    private void onStartGameMessageReceived() {
        mWaitingRoomFinishedFromCode = true;
        finishActivity(RC_WAITING_ROOM);
    }

    private void signOut() {
        GoogleSignInClient signInClient = GoogleSignIn.getClient(this,
                GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
        signInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // at this point, the user is signed out.
                        showToast("Signed out completed.");
                        finish();
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
    }
    @Override
    public void onPause(){
        super.onPause();
        if(mMediaPlayer!=null)mMediaPlayer.pause();//can be null when user navigates to other fragments.
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        cleanUpHomeScreen();
    }

    private void cleanUpHomeScreen() {
        if(mMediaPlayer!=null) mMediaPlayer.release();
        mMediaPlayer=null;
    }

    private Animation bounceAnimation(long duration){
        TranslateAnimation animation = new TranslateAnimation(0,0,30,0);
        //animation.setStartOffset(offsetTime);
        animation.setInterpolator(new BounceInterpolator());
        animation.setDuration(duration);
        animation.setRepeatCount(Animation.INFINITE);
        animation.start();
        return animation;
    }
}
