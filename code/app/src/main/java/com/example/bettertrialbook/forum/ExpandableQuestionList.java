package com.example.bettertrialbook.forum;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.example.bettertrialbook.R;
import com.example.bettertrialbook.dal.Callback;
import com.example.bettertrialbook.dal.UserDAL;
import com.example.bettertrialbook.models.Question;
import com.example.bettertrialbook.models.Reply;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays a list of questions and allows users to tap on them to expand.
 * When expanded, this will display the question's body, replies, and a reply button.
 */
public class ExpandableQuestionList extends BaseExpandableListAdapter {
    Context context;
    Callback<Question> postReply;
    List<Question> questions;

    public ExpandableQuestionList(@NonNull Context context, Callback<Question> postReply) {
        this.context = context;
        this.postReply = postReply;
        this.questions = new ArrayList<>();
    }

    /**
     * Adds the list of questions to the local list to update/display
     * @param questions
     */
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
        this.notifyDataSetChanged();
    }

    // Used to determine how many questions should be rendered
    @Override
    public int getGroupCount() {
        return questions.size();
    }

    // Used to determine how many children should be drawn when a question is expanded
    @Override
    public int getChildrenCount(int groupPosition) {
        // 1 child for the question body/reply button, and another child for each reply
        return questions.get(groupPosition).getReplies().size() + 1;
    }

    @Override
    public Question getGroup(int groupPosition) {
        return questions.get(groupPosition);
    }

    @Override
    public Reply getChild(int groupPosition, int childPosition) {
        // First child is not a reply
        if (childPosition == 0)
            return null;
        return getGroup(groupPosition).getReplies().get(childPosition - 1);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(this.context);
            view = inflater.inflate(R.layout.question_list, parent, false);
        }

        TextView title = view.findViewById(R.id.q_title);
        TextView replyCount = view.findViewById(R.id.q_reply_count);
        TextView poster = view.findViewById(R.id.q_poster);

        Question question = getGroup(groupPosition);
        title.setText(question.getTitle());
        replyCount.setText(question.getReplies().size() + " Response(s)");
        poster.setText("By: " + question.getPosterId().substring(0, 4));
        new UserDAL().findUserByID(question.getPosterId(), user -> poster.setText("By: "+ user.getUsername()));

        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Question q = getGroup(groupPosition);
        if (childPosition == 0) {
            return getQuestionDetailsView(q, convertView, parent);
        }
        return getReplyView(getChild(groupPosition, childPosition), convertView, parent);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    /**
     * create a new view if the view hasn't been created yet or if we are passed a view with an incorrect layout
     * @param q
     * @param view
     * @param parent
     * @return
     */
    private View getQuestionDetailsView(Question q, View view, ViewGroup parent) {
        if (view == null || view.findViewById(R.id.post_reply) == null) {
            view = inflateLayout(R.layout.question_details, parent);
        }
        TextView body = view.findViewById(R.id.q_body);
        Button replyButton = view.findViewById(R.id.post_reply);
        replyButton.setOnClickListener(v -> postReply.execute(q));
        body.setText(q.getText());

        return view;
    }

    /**
     * create a new view if the view hasn't been created yet or if we are passed a view with an incorrect layout
     * @param r
     * @param view
     * @param parent
     * @return
     */
    private View getReplyView(Reply r, View view, ViewGroup parent) {
        if (view == null || view.findViewById(R.id.reply_body) == null) {
            view = inflateLayout(R.layout.reply, parent);
        }

        TextView body = view.findViewById(R.id.reply_body);
        TextView poster = view.findViewById(R.id.reply_poster);

        body.setText(r.getText());
        poster.setText("By: " + r.getPosterId().substring(0, 4));
        new UserDAL().findUserByID(r.getPosterId(), user -> poster.setText("By: "+ user.getUsername()));

        return view;
    }

    /**
     * inflates the layout from the parent
     * @param layout
     * @param parent
     * @return
     */
    private View inflateLayout(@LayoutRes int layout, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        return inflater.inflate(layout, parent, false);
    }
}
