package com.example.bettertrialbook.forum;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bettertrialbook.R;
import com.example.bettertrialbook.models.Question;

/**
 * Displays a list of questions, expands them to show replies and reply button when tapped.
 */
public class QuestionList extends ArrayAdapter<Question> {
   private Context context;
   private Question expandedQuestion;


   public QuestionList(@NonNull Context context) {
      super(context, 0);
      this.context = context;
   }

   @NonNull
   @Override
   public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       View view = convertView;

       if (view == null){
          view = LayoutInflater.from(getContext()).inflate(R.layout.question_list, parent, false);
       }

       Question question = this.getItem(position);
       TextView title = view.findViewById(R.id.q_title);
       TextView replyCount = view.findViewById(R.id.q_reply_count);
       TextView poster = view.findViewById(R.id.q_poster);


       title.setText(question.getTitle());
       replyCount.setText(question.getReplies().size() + " Response(s)");
       poster.setText("By: " + question.getPosterId());

       title.setOnClickListener(titleView -> toggleExpandQuestion(question));
//       if (questionIsExpanded(question))
//           showExpandedQuestion(view, question);
       return view;
   }

   private void toggleExpandQuestion(Question question) {
       // collapse the question if it was already opened
       if (expandedQuestion != null && questionIsExpanded(question))
           expandedQuestion = null;
       else expandedQuestion = question;
   }

   private boolean questionIsExpanded(Question q) {
       return expandedQuestion.getId().equals(q.getId());
   }

   private void showExpandedQuestion(View view, Question q) {
       View expanded = view.findViewById(R.id.q_expanded);
       expanded.setVisibility(View.VISIBLE);
       TextView body = view.findViewById(R.id.q_body);
       body.setText(q.getText());
   }
}
