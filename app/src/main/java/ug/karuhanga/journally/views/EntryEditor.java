package ug.karuhanga.journally.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.github.irshulx.Editor;
import com.github.irshulx.models.EditorTextStyle;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import ug.karuhanga.journally.R;
import ug.karuhanga.journally.controllers.EditorControllerDash;
import ug.karuhanga.journally.models.Entry;

import static ug.karuhanga.journally.utils.Utils.log;

@EActivity(R.layout.activity_editor)
public class EntryEditor extends AppCompatActivity implements EditorControllerDash {

    public static final String IS_NEW = "is_new";
    private static final long DEFAULT_VALUE_NULL = -100;
    public static String PK = "pk";
    @ViewById(R.id.editor)
    Editor editor;

    @ViewById(R.id.edit_text_title_editor)
    EditText title;
    EntryEditorController controller;

    public static void launch(Context context) {
        Intent intent = new Intent(context, EntryEditor_.class);
        intent.putExtra(IS_NEW, true);
        context.startActivity(intent);
    }

    public static void launch(Context context, long pk) {
        Intent intent = new Intent(context, EntryEditor_.class);
        intent.putExtra(IS_NEW, false);
        intent.putExtra(PK, pk);
        context.startActivity(intent);
    }

    @Override
    protected void onPause() {
        log("Saving...");
        save();
        super.onPause();
    }

    @Background
    void save() {
        getController().save(title.getText().toString(), editor.getContentAsSerialized());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setController(new ug.karuhanga.journally.controllers.EntryEditorController(this));
        getController().setStatus(getIntent().getBooleanExtra(IS_NEW, true));
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void init() {
        setUpEditor();
        fetchData();
    }

    @Background
    void fetchData() {
        getController().fetchData();
    }

    public EntryEditorController getController() {
        return controller;
    }

    public void setController(ug.karuhanga.journally.controllers.EntryEditorController controller) {
        this.controller = controller;
    }

    @Override
    public long getPk() {
        return getIntent().getLongExtra(PK, DEFAULT_VALUE_NULL);
    }

    @Override
    public Context requestContext() {
        return this;
    }

    @UiThread
    @Override
    public void onDataReady(Entry entry) {
        this.title.setText(entry.getTitle());
        this.editor.render(editor.getContentDeserialized(entry.getText()));
    }

    @UiThread
    @Override
    public void onNewDataReady(Entry entry) {
        this.editor.render();
    }

    private void setUpEditor() {
        findViewById(R.id.action_h1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.H1);
            }
        });

        findViewById(R.id.action_h2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.H2);
            }
        });

        findViewById(R.id.action_h3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.H3);
            }
        });

        findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.BOLD);
            }
        });

        findViewById(R.id.action_Italic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.ITALIC);
            }
        });

        findViewById(R.id.action_indent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.INDENT);
            }
        });

        findViewById(R.id.action_outdent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.OUTDENT);
            }
        });

        findViewById(R.id.action_bulleted).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.insertList(false);
            }
        });

        findViewById(R.id.action_unordered_numbered).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.insertList(true);
            }
        });

        findViewById(R.id.action_hr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.insertDivider();
            }
        });

        findViewById(R.id.action_insert_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.openImagePicker();
            }
        });

        findViewById(R.id.action_insert_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.insertLink();
            }
        });

        findViewById(R.id.action_map).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.insertMap();
            }
        });

        findViewById(R.id.action_erase).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.clearAllContents();
            }
        });
    }

    public interface EntryEditorController {

        void fetchData();

        void setStatus(boolean isNew);

        void save(String title, String text);
    }
}
