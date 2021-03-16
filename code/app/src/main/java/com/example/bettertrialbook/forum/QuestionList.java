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

public class QuestionList extends ArrayAdapter<Question> {
   private Context context;


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

       title.setText(question.getTitle());
       replyCount.setText(question.getReplies().size() + " Response(s)");
       return view;
   }
}
